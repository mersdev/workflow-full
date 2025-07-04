# Workflow Full - SPAKE2+ Owner Pairing Simulation

This project, `mersdev-workflow-full`, demonstrates a simulated owner pairing process between a device and a vehicle using the SPAKE2+ protocol, orchestrated by Temporal.io workflows. It consists of two main Spring Boot applications: `workflow-device` and `workflow-vehicle`.

## Table of Contents

1.  [Project Overview](#project-overview)
2.  [Architecture](#architecture)
3.  [Core Technologies](#core-technologies)
4.  [Directory Structure](#directory-structure)
5.  [Module Details](#module-details)
    *   [workflow-device](#workflow-device-application)
    *   [workflow-vehicle](#workflow-vehicle-application)
6.  [Workflow Explanations](#workflow-explanations)
    *   [SPAKE2+ Protocol](#spake2-protocol)
    *   [Self-Contained Full Cycle Pairing](#self-contained-full-cycle-pairing)
    *   [Inter-Service Owner Pairing](#inter-service-owner-pairing)
7.  [Message Format (TLV)](#message-format-tlv)
8.  [Setup and Running the Project](#setup-and-running-the-project)
    *   [Prerequisites](#prerequisites)
    *   [Temporal Setup](#temporal-setup)
    *   [Building the Applications](#building-the-applications)
    *   [Running the Applications](#running-the-applications)
9.  [Testing the Flow](#testing-the-flow)
    *   [Using `test.http`](#using-testhttp)
10. [Key Components Overview](#key-components-overview)
11. [Docker Compose Files](#docker-compose-files)
12. [Gradle Configuration](#gradle-configuration)

## 1. Project Overview

The primary goal of this project is to simulate the cryptographic handshake involved in pairing a device (e.g., a smartphone acting as a digital key) with a vehicle using the SPAKE2+ (Secure Password Authenticated Key Exchange) protocol. This pairing process is essential for establishing a secure channel and shared secrets for subsequent digital key operations.

The simulation involves two microservices:
*   `workflow-device`: Represents the device side of the pairing.
*   `workflow-vehicle`: Represents the vehicle side of the pairing.

Both applications use [Temporal.io](https://temporal.io/) to manage and orchestrate the multi-step pairing workflows. Messages exchanged during the SPAKE2+ protocol are encoded using the Tag-Length-Value (TLV) format.

## 2. Architecture

The system consists of two main applications that can communicate with each other over HTTP. Each application has its own instance of a Temporal cluster (provided via Docker Compose) to manage its workflows.

```
+---------------------+      HTTP (SPAKE2+ TLV)      +----------------------+
|   workflow-device   |<--------------------------->|   workflow-vehicle   |
| (localhost:3030)    | (Feign: DkcClient/SbodClient)|  (localhost:3031)    |
+---------------------+                              +----------------------+
          |                                                      |
          | (Temporal Client)                                    | (Temporal Client)
          v                                                      v
+---------------------+                              +----------------------+
| Temporal Cluster    |                              | Temporal Cluster     |
| (Device Specific)   |                              | (Vehicle Specific)   |
| (localhost:7234/8081)                              | (localhost:7233/8080)|
+---------------------+                              +----------------------+
```

**Interactions:**
1.  **Full Cycle (Internal Test):** Each application can run a full SPAKE2+ pairing cycle internally, simulating both device and vehicle roles. This is triggered by the `/startFullOwnerPairingCycle` endpoints.
2.  **Inter-Service Pairing:** A more realistic pairing involves one application initiating the process (e.g., vehicle via `/startOwnerPairing`) and exchanging messages with the other application step-by-step.
    *   `workflow-vehicle` uses `SbodClient` to send messages to `workflow-device`.
    *   `workflow-device` uses `DkcClient` to send messages to `workflow-vehicle`.
    *   The `x-requestId` header is crucial for correlating workflow instances across services.

## 3. Core Technologies

*   **Java 17+** with **Spring Boot 3+**
*   **Temporal.io:** For workflow orchestration.
*   **SPAKE2+ Protocol:** For secure key exchange. Cryptographic operations are implemented using **BouncyCastle**.
*   **TLV (Tag-Length-Value):** Message encoding format, implemented using the `com.payneteasy.tlv` library.
*   **Feign Clients:** For declarative REST API communication between services.
*   **Gradle:** Build automation tool.
*   **Docker & Docker Compose:** For setting up Temporal.io environments.
*   **Lombok:** To reduce boilerplate code.

## 4. Directory Structure

```
└── mersdev-workflow-full/
    ├── README.md                 # This file
    ├── test.http                 # HTTP requests for testing
    ├── workflow-device/          # Device simulation application
    │   ├── docker-compose-device.yml
    │   ├── gradlew, gradlew.bat, gradle/
    │   └── src/
    │       ├── main/java/com/xdman/workflow_device/ # Main source code
    │       └── main/resources/application.yaml      # Spring Boot config
    └── workflow-vehicle/         # Vehicle simulation application
        ├── docker-compose-vehicle.yaml
        ├── gradlew, gradlew.bat, gradle/
        └── src/
            ├── main/java/com/xdman/workflow_vehicle/ # Main source code
            └── main/resources/application.yaml       # Spring Boot config
```

## 5. Module Details

Both `workflow-device` and `workflow-vehicle` share a similar internal structure due to the symmetric nature of the SPAKE2+ protocol and the need for each to potentially simulate both roles for testing.

### `workflow-device` Application

*   **Purpose:** Simulates a mobile device (e.g., a smartphone acting as a digital key).
*   **Port:** Runs on `localhost:3030` (configurable in `application.yaml`).
*   **Key Packages & Classes:**
    *   `controllers/DeviceOEMController.java`: Exposes REST endpoints:
        *   `/startFullOwnerPairingCycle/{vin}`: Initiates an internal full SPAKE2+ pairing cycle.
        *   `/receivedFromVehicle/{vin}`: Receives messages (SPAKE2+ Request or Verify commands) from the vehicle.
    *   `service/`:
        *   `Spake2PlusDeviceService.java`: Implements the **device's** cryptographic logic for the SPAKE2+ protocol (processing requests, generating responses, verifying vehicle evidence).
        *   `Spake2PlusVehicleService.java`: Implements the **vehicle's** cryptographic logic. This allows the device application to simulate vehicle steps, primarily for the internal "full cycle" workflow.
        *   `DkcService.java`: Uses `DkcClient` to send command messages to the `workflow-vehicle` application.
        *   `ReceivedFromVehicleService.java`: Handles incoming messages from the vehicle, determines the message type (SPAKE2+ Request or Verify), and initiates or signals the appropriate Temporal workflow (`Spake2PlusDeviceWorkFlow`).
    *   `client/`:
        *   `DkcClient.java`: Feign client for communicating with `workflow-vehicle` (specifically its `/sendToVehicle` endpoint).
        *   `DkcFallbackFactory.java`: Fallback mechanism for `DkcClient`.
    *   `config/`:
        *   `DkcFeignClientConfig.java`: Configures Feign client, including adding necessary headers like `x-requestId`.
        *   `WorkFlowConfig.java`: Provides default configurations for Temporal Workflows and Activities (e.g., retry options, timeouts).
    *   `workflow/`:
        *   `Spake2PlusDeviceWorkFlow.java` (Interface) & `Spake2PlusDeviceWorkFlowImpl.java`: Defines and implements the Temporal workflow for the device's role in an inter-service owner pairing. It handles receiving SPAKE2+ request and verify messages, and orchestrates activities to process them and send responses.
        *   `Spake2PlusFullWorkFlow.java` (Interface) & `Spake2PlusFullWorkFlowImpl.java`: Defines and implements a Temporal workflow that executes a complete SPAKE2+ pairing cycle *internally* within the `workflow-device` application by calling activities that simulate both device and vehicle steps.
    *   `activity/`:
        *   `Spake2PlusDeviceActivity.java` (Interface) & `Spake2PlusDeviceActivityImpl.java`: Temporal activities for device-specific SPAKE2+ operations (e.g., receiving/processing SPAKE2+ requests/verifications from vehicle, sending responses via `DkcService`).
        *   `Spake2PlusVehicleActivity.java` (Interface) & `Spake2PlusVehicleActivityImpl.java`: Temporal activities for vehicle-specific SPAKE2+ operations (used by `Spake2PlusFullWorkFlow` to simulate vehicle actions).
    *   `model/`: Contains data transfer objects (DTOs) for API requests/responses, SPAKE2+ protocol data (`Spake2PlusDeviceData`, `Spake2PlusVehicleData`), and TLV model classes.
        *   `model/tlv/`: Defines structures for SPAKE2+ messages in TLV format (e.g., `Spake2PlusRequestCommandTlv`, `Spake2PlusVerifyResponseTlv`).
    *   `docker-compose-device.yml`: Docker Compose file to set up a dedicated Temporal environment (PostgreSQL, Elasticsearch, Temporal Server, Temporal UI) for the device application.
        *   Temporal Server: `localhost:7234`
        *   Temporal UI: `http://localhost:8081`

### `workflow-vehicle` Application

*   **Purpose:** Simulates a vehicle's head unit or telematics control unit.
*   **Port:** Runs on `localhost:3031` (configurable in `application.yaml`).
*   **Key Packages & Classes:**
    *   `controllers/VehicleOEMController.java`: Exposes REST endpoints:
        *   `/startFullOwnerPairingCycle/{vin}`: Initiates an internal full SPAKE2+ pairing cycle.
        *   `/sendToVehicle/{vin}`: Receives messages (SPAKE2+ Response or Verify commands) from the device.
        *   `/startOwnerPairing/{vin}`: Initiates an inter-service owner pairing workflow where the vehicle starts the SPAKE2+ exchange.
        *   `/testSbodReceiveFromVehicle/{vin}`: A test endpoint.
    *   `service/`:
        *   `Spake2PlusVehicleService.java`: Implements the **vehicle's** cryptographic logic for the SPAKE2+ protocol (creating requests, processing responses, creating verify commands).
        *   `Spake2PlusDeviceService.java`: Implements the **device's** cryptographic logic. This allows the vehicle application to simulate device steps, primarily for the internal "full cycle" workflow.
        *   `SbodService.java`: Uses `SbodClient` to send command messages to the `workflow-device` application.
        *   `SendToVehicleService.java`: Handles requests to send messages to the vehicle (itself, via signals to workflows) or to initiate pairing workflows (`Spake2PlusVehicleWorkFlow`, `Spake2PlusFullWorkFlow`).
    *   `client/`:
        *   `SbodClient.java`: Feign client for communicating with `workflow-device` (specifically its `/receivedFromVehicle` endpoint).
        *   `SbodFallbackFactory.java`: Fallback mechanism for `SbodClient`.
    *   `config/`:
        *   `SbodFeignClientConfig.java`: Configures Feign client, adding headers like `x-requestId`.
        *   `WorkFlowConfig.java`: Shared Temporal configuration.
    *   `workflow/`:
        *   `Spake2PlusVehicleWorkFlow.java` (Interface) & `Spake2PlusVehicleWorkFlowImpl.java`: Defines and implements the Temporal workflow for the vehicle's role in an inter-service owner pairing. It initiates the pairing by creating and sending a SPAKE2+ request, then handles responses and subsequent verification steps.
        *   `Spake2PlusFullWorkFlow.java` (Interface) & `Spake2PlusFullWorkFlowImpl.java`: Temporal workflow for an internal, complete SPAKE2+ pairing cycle simulation within `workflow-vehicle`.
    *   `activity/`:
        *   `Spake2PlusVehicleActivity.java` (Interface) & `Spake2PlusVehicleActivityImpl.java`: Temporal activities for vehicle-specific SPAKE2+ operations (e.g., creating/sending SPAKE2+ requests/verifications to device via `SbodService`, receiving responses).
        *   `Spake2PlusDeviceActivity.java` (Interface) & `Spake2PlusDeviceActivityImpl.java`: Temporal activities for device-specific SPAKE2+ operations (used by `Spake2PlusFullWorkFlow` to simulate device actions).
    *   `model/`: Similar structure to `workflow-device`, containing DTOs and TLV models.
    *   `docker-compose-vehicle.yaml`: Docker Compose file for a dedicated Temporal environment for the vehicle application.
        *   Temporal Server: `localhost:7233`
        *   Temporal UI: `http://localhost:8080`

## 6. Workflow Explanations

### SPAKE2+ Protocol

The SPAKE2+ protocol is a password-authenticated key exchange (PAKE) protocol. At a high level, for this owner pairing scenario, it involves:
1.  **Password Derivation (Scrypt):** Both device and vehicle use a pre-shared password and salt with Scrypt to derive keying material (`w0`, `w1`).
2.  **Request Phase:**
    *   One party (e.g., Vehicle) sends a `SPAKE2+ Request Command` containing its Scrypt parameters, supported versions, etc.
    *   The other party (e.g., Device) receives this, processes it, generates its public value `X` (derived from its Scrypt material and a random scalar `x`), and sends back a `SPAKE2+ Request Response` containing `X`.
3.  **Verification Phase:**
    *   The first party (Vehicle) receives `X`, generates its public value `Y` (derived from its Scrypt material and a random scalar `y`), computes a shared secret, and derives evidence keys. It then sends a `SPAKE2+ Verify Command` containing `Y` and its evidence `M1`.
    *   The second party (Device) receives `Y` and `M1`, computes the shared secret, derives evidence keys, verifies `M1`, and if successful, computes its own evidence `M2`. It sends back a `SPAKE2+ Verify Response` containing `M2`.
    *   The first party (Vehicle) verifies `M2`.

If both evidences are verified, both parties have established a shared secret. The `Spake2PlusDeviceService` and `Spake2PlusVehicleService` in both modules implement the cryptographic steps (elliptic curve operations, hashing, CMAC) based on NIST P-256 curve and SHA-256, following conventions likely from a digital key specification (indicated by references to "Listings" in the service code comments).

### Self-Contained Full Cycle Pairing

*   **Triggered by:**
    *   `POST /startFullOwnerPairingCycle/{vin}` endpoint in `workflow-device`.
    *   `POST /startFullOwnerPairingCycle/{vin}` endpoint in `workflow-vehicle`.
*   **Workflow:** `Spake2PlusFullWorkFlow` (specific to the application it's running in).
*   **Execution:** This workflow runs entirely within the microservice it was triggered in. It uses local activities that call `Spake2PlusDeviceService` and `Spake2PlusVehicleService` (both available within the same application) to simulate all steps of the SPAKE2+ protocol from both device and vehicle perspectives.
*   **Purpose:** Useful for testing the complete SPAKE2+ logic and Temporal workflow orchestration without actual inter-service HTTP calls.

### Inter-Service Owner Pairing

This represents a more realistic pairing scenario where the device and vehicle applications communicate over the network.

**Example: Vehicle-Initiated Pairing**
1.  **Initiation (Vehicle):**
    *   Client sends `POST /startOwnerPairing/{vin}` to `workflow-vehicle` (port 3031).
    *   `SendToVehicleService` starts the `Spake2PlusVehicleWorkFlow` on the vehicle's Temporal.
2.  **Vehicle Workflow (`Spake2PlusVehicleWorkFlowImpl` on Vehicle's Temporal):**
    *   **Step 1: Create & Send SPAKE2+ Request:**
        *   `Spake2PlusVehicleActivity.createSpake2PlusRequestSuccessfully`: Uses local `Spake2PlusVehicleService` to generate the `Spake2PlusRequestCommandTlv`.
        *   `Spake2PlusVehicleActivity.sendSpake2PlusRequestSuccessfully`: Uses `SbodService` (which uses `SbodClient`) to send this TLV message via HTTP POST to `workflow-device`'s `/receivedFromVehicle/{vin}` endpoint (port 3030).
    *   The workflow then `Workflow.await()` for a signal containing the device's response.
3.  **Device Processing (`workflow-device`):**
    *   `DeviceOEMController` receives the HTTP request at `/receivedFromVehicle/{vin}`.
    *   `ReceivedFromVehicleService.receiveMessageFromVehicle` identifies it as a SPAKE2+ Request (`8030...` header).
    *   It starts/signals the `Spake2PlusDeviceWorkFlow` on the device's Temporal (using `x-requestId` for correlation).
4.  **Device Workflow (`Spake2PlusDeviceWorkFlowImpl` on Device's Temporal):**
    *   **Step 2: Process Request & Send SPAKE2+ Response:**
        *   `Spake2PlusDeviceActivity.receiveSpake2PlusRequestCommandSuccessfully`: Decodes the incoming TLV.
        *   `Spake2PlusDeviceActivity.processSpake2PlusRequestSuccessfully`: Uses local `Spake2PlusDeviceService` to process the request and generate the `Spake2PlusRequestResponseTlv`.
        *   `Spake2PlusDeviceActivity.sendSpake2PlusResponseSuccessfully`: Uses `DkcService` (which uses `DkcClient`) to send this response TLV via HTTP POST to `workflow-vehicle`'s `/sendToVehicle/{vin}` endpoint (port 3031).
    *   The workflow then `Workflow.await()` for a signal containing the vehicle's verify command.
5.  **Vehicle Workflow (Resumes on Vehicle's Temporal):**
    *   `workflow-vehicle` receives the HTTP response at `/sendToVehicle/{vin}`.
    *   `SendToVehicleService.sendToVehicle` signals the waiting `Spake2PlusVehicleWorkFlow` with the received message.
    *   **Step 3: Process Response, Create & Send SPAKE2+ Verify:**
        *   `Spake2PlusVehicleActivity.receiveSpake2PlusResponseSuccessfully`: Decodes the device's response.
        *   `Spake2PlusVehicleActivity.createSpake2PlusVerifyCommandSuccessfully`: Uses local `Spake2PlusVehicleService` to generate the `Spake2PlusVerifyCommandTlv`.
        *   `Spake2PlusVehicleActivity.sendSpake2PlusVerifyCommandSuccessfully`: Sends this verify command TLV via `SbodClient` to `workflow-device`'s `/receivedFromVehicle/{vin}`.
    *   The workflow `Workflow.await()` for the device's final verification response.
6.  **Device Workflow (Resumes on Device's Temporal):**
    *   `workflow-device` receives the verify command.
    *   `ReceivedFromVehicleService` identifies it as SPAKE2+ Verify (`8032...`) and signals the `Spake2PlusDeviceWorkFlow`.
    *   **Step 4: Process Verify & Send SPAKE2+ Verify Response:**
        *   `Spake2PlusDeviceActivity.receiveSpake2PlusVerifyCommandSuccessfully`: Decodes the verify command.
        *   `Spake2PlusDeviceActivity.processSpake2PlusVerifyCommandSuccessfully`: Uses local `Spake2PlusDeviceService` to process the verification and generate the `Spake2PlusVerifyResponseTlv`.
        *   `Spake2PlusDeviceActivity.sendSpake2PlusVerifyResponseSuccessfully`: Sends this final response TLV via `DkcClient` to `workflow-vehicle`'s `/sendToVehicle/{vin}`.
7.  **Vehicle Workflow (Resumes & Completes on Vehicle's Temporal):**
    *   `workflow-vehicle` receives the final verify response.
    *   `SendToVehicleService` signals the `Spake2PlusVehicleWorkFlow`.
    *   `Spake2PlusVehicleActivity.receiveSpake2PlusVerifyResponseCommandSuccessfully`: Decodes and processes the final response.
    *   The pairing is complete.

A similar flow would occur if the device initiated the pairing, though specific endpoints/workflows for device-initiated inter-service pairing are not as explicitly shown in `test.http` as the vehicle-initiated one. The `Spake2PlusDeviceWorkFlow` is designed to react to incoming messages.

## 7. Message Format (TLV)

Communications for the SPAKE2+ protocol steps primarily use Tag-Length-Value (TLV) encoding.
*   The `com.xdman.workflow_*/model/tlv/` package in each module contains Java classes representing these TLV structures (e.g., `Spake2PlusRequestCommandTlv.java`, `Spake2PlusRequestResponseTlv.java`, `Spake2PlusVerifyCommandTlv.java`, `Spake2PlusVerifyResponseTlv.java`).
*   These classes use the `payneteasy-tlv` library for encoding to a hex string and decoding from a hex string.
*   The TLV structures adhere to specifications for Digital Key systems, defining tags for various data elements like Scrypt parameters, curve points, and cryptographic evidence.
*   Example command APDU structure: `80 30 00 00 Lc [Data] 00` (for SPAKE2+ Request) or `80 32 00 00 Lc [Data] 00` (for SPAKE2+ Verify).
*   Example response APDU structure: `[Data] 90 00`.

## 8. Setup and Running the Project

### Prerequisites

*   Java JDK 17 or higher
*   Gradle 8.x (via wrapper)
*   Docker
*   Docker Compose

### Temporal Setup

Each application (`workflow-device` and `workflow-vehicle`) requires its own Temporal cluster. Docker Compose files are provided for this.

**1. For `workflow-device`:**
Open a terminal in the `mersdev-workflow-full/workflow-device/` directory and run:
   ```bash
   docker-compose -f docker-compose-device.yml up -d
   ```
This will start:
*   Temporal Server on port `7234`
*   Temporal Web UI on `http://localhost:8081`
*   Elasticsearch and PostgreSQL as dependencies for Temporal.

**2. For `workflow-vehicle`:**
Open another terminal in the `mersdev-workflow-full/workflow-vehicle/` directory and run:
   ```bash
   docker-compose -f docker-compose-vehicle.yaml up -d
   ```
This will start:
*   Temporal Server on port `7233`
*   Temporal Web UI on `http://localhost:8080`
*   Elasticsearch and PostgreSQL as dependencies for Temporal.

Ensure these ports do not conflict with other services on your machine.

### Building the Applications

Navigate to each module's directory (`workflow-device` and `workflow-vehicle`) and run the Gradle build command:

For `workflow-device`:
```bash
cd workflow-device
./gradlew build
```

For `workflow-vehicle`:
```bash
cd workflow-vehicle
./gradlew build
```

### Running the Applications

After building, you can run each Spring Boot application.

For `workflow-device` (from `workflow-device` directory):
```bash
./gradlew bootRun
# OR
java -jar build/libs/workflow-device-0.0.1-SNAPSHOT.jar
```
This application will start on `http://localhost:3030`.

For `workflow-vehicle` (from `workflow-vehicle` directory):
```bash
./gradlew bootRun
# OR
java -jar build/libs/workflow-vehicle-0.0.1-SNAPSHOT.jar
```
This application will start on `http://localhost:3031`.

**Configuration:**
Application-specific configurations (server ports, Temporal connection details) are in:
*   `workflow-device/src/main/resources/application.yaml`
*   `workflow-vehicle/src/main/resources/application.yaml`

## 9. Testing the Flow

### Using `test.http`

The `test.http` file in the root directory contains sample HTTP requests to test the various pairing flows. You can use an IDE with an HTTP client plugin (like IntelliJ IDEA's built-in client or VS Code's REST Client extension) to execute these requests.

**Example Requests from `test.http`:**

*   **[Device] - Full Owner Pairing Cycle (Internal Test):**
    ```http
    POST http://localhost:3030/startFullOwnerPairingCycle/WX12345D
    Content-Type: application/json
    x-requestId: full-flow-device-123

    {
      "salt": "000102030405060708090A0B0C0D0E0F",
      "password": "0102030405060708090A0B0C0D0E0F10"
    }
    ```
    This triggers the `Spake2PlusFullWorkFlow` within `workflow-device`. Check the application logs and the Device Temporal UI (`http://localhost:8081`) for workflow execution.

*   **[Vehicle] - Full Owner Pairing Cycle (Internal Test):**
    ```http
    POST http://localhost:3031/startFullOwnerPairingCycle/WX12345V
    Content-Type: application/json
    x-requestId: full-flow-vehicle-123

    {
      "salt": "000102030405060708090A0B0C0D0E0F",
      "password": "0102030405060708090A0B0C0D0E0F10"
    }
    ```
    This triggers the `Spake2PlusFullWorkFlow` within `workflow-vehicle`. Check logs and Vehicle Temporal UI (`http://localhost:8080`).

*   **[Vehicle] - Kickstart Inter-Service Owner Pairing:**
    ```http
    POST http://localhost:3031/startOwnerPairing/WX12345TEST
    Content-Type: application/json
    x-requestId: inter-service-pairing-123

    {
      "salt": "000102030405060708090A0B0C0D0E0F",
      "password": "0102030405060708090A0B0C0D0E0F10"
    }
    ```
    This initiates the vehicle-led inter-service pairing.
    1.  `workflow-vehicle` starts `Spake2PlusVehicleWorkFlow` (visible in Vehicle Temporal UI).
    2.  `workflow-vehicle` sends a SPAKE2+ Request to `workflow-device`.
    3.  `workflow-device` starts `Spake2PlusDeviceWorkFlow` (visible in Device Temporal UI, correlated by `x-requestId` set by Feign interceptor, e.g., `7d89f678-test-4b51-b4d7a` if not overridden by the test request).
    4.  Messages are exchanged back and forth until pairing completes. Monitor logs of both applications and both Temporal UIs.

*   **Simulating Device Receiving SPAKE2+ Request from Vehicle:**
    (This is what `workflow-vehicle` calls during inter-service pairing)
    ```http
    POST http://localhost:3030/receivedFromVehicle/WX12345
    Content-Type: application/json
    x-requestId: inter-service-pairing-123 // Should match the ID used by vehicle workflow

    {
      "message": "803000002F5B0201005C0201007F5020C0100102030405060708090A0B0C0D0E0F10C10400001000C2020008C3020001D602000300"
    }
    ```

*   **Simulating Device Receiving SPAKE2+ Verify from Vehicle:**
    ```http
    POST http://localhost:3030/receivedFromVehicle/WX12345
    Content-Type: application/json
    x-requestId: inter-service-pairing-123 // Should match

    {
      "message": "8032000055524104D8635EFDD44C8E2B6D44C500C3375B13B40C9F83E239162D499F5EB2850BBE6C9F024FC7C5A953F400C3DDEDF22FF554C64652DC15DAE8515C8D8B054801F24057103B1730D80E0603BC690A65806123676C00"
    }
    ```

**Verification:**
*   Check application console logs for output from services, workflows, and activities.
*   Use the Temporal Web UIs (`http://localhost:8081` for device, `http://localhost:8080` for vehicle) to inspect workflow executions, their history, inputs, and outputs.
*   Successful pairing will typically log messages indicating completion and potentially derived keys (though sensitive keys should not be logged in production).

## 10. Key Components Overview

| Module             | Component                                     | Role                                                                                                |
| ------------------ | --------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| **Shared (Both)**  | `model/tlv/`                                  | TLV data structures for SPAKE2+ messages.                                                           |
|                    | `config/WorkFlowConfig.java`                  | Default Temporal workflow/activity options.                                                         |
|                    | `service/Spake2PlusDeviceService.java`        | Implements device-side SPAKE2+ crypto logic.                                                        |
|                    | `service/Spake2PlusVehicleService.java`       | Implements vehicle-side SPAKE2+ crypto logic.                                                       |
|                    | `workflow/Spake2PlusFullWorkFlow.java`        | Orchestrates an internal full SPAKE2+ cycle.                                                        |
|                    | `activity/Spake2PlusDeviceActivity.java`      | Temporal activities for device's SPAKE2+ steps.                                                     |
|                    | `activity/Spake2PlusVehicleActivity.java`     | Temporal activities for vehicle's SPAKE2+ steps.                                                    |
| **workflow-device**| `controllers/DeviceOEMController.java`        | Exposes API for device interactions.                                                                |
|                    | `service/DkcService.java`                     | Sends messages to vehicle via `DkcClient`.                                                          |
|                    | `client/DkcClient.java`                       | Feign client to call `workflow-vehicle`.                                                            |
|                    | `workflow/Spake2PlusDeviceWorkFlow.java`      | Orchestrates device's role in inter-service pairing (reactive).                                     |
| **workflow-vehicle**| `controllers/VehicleOEMController.java`       | Exposes API for vehicle interactions.                                                               |
|                    | `service/SbodService.java`                    | Sends messages to device via `SbodClient`.                                                          |
|                    | `client/SbodClient.java`                      | Feign client to call `workflow-device`.                                                             |
|                    | `workflow/Spake2PlusVehicleWorkFlow.java`     | Orchestrates vehicle's role in inter-service pairing (initiator/responder).                         |

## 11. Docker Compose Files

*   `workflow-device/docker-compose-device.yml`:
    *   Sets up Temporal, Elasticsearch, and PostgreSQL for `workflow-device`.
    *   Network: `temporal-device-network`.
    *   Temporal Server: Exposed on host port `7234`.
    *   Temporal UI: Exposed on host port `8081`.
*   `workflow-vehicle/docker-compose-vehicle.yaml`:
    *   Sets up Temporal, Elasticsearch, and PostgreSQL for `workflow-vehicle`.
    *   Network: `temporal-network`.
    *   Temporal Server: Exposed on host port `7233`.
    *   Temporal UI: Exposed on host port `8080`.

These separate environments allow each microservice to have its own isolated Temporal backend, simulating a more distributed setup.

## 12. Gradle Configuration

*   `gradlew`, `gradlew.bat`: Gradle wrapper scripts to build and run tasks without needing a local Gradle installation.
*   `gradle/wrapper/gradle-wrapper.properties`: Specifies the Gradle version to be used.
*   Each module (`workflow-device`, `workflow-vehicle`) has its own `build.gradle` (implicitly, as it's a standard Gradle project structure) defining dependencies (Spring Boot, Temporal, Feign, BouncyCastle, Payara TLV, Lombok) and build configurations.

---
