# DevTool - Temporal Workflow Pod Management

This devtool folder provides a comprehensive, organized way to manage Temporal workflow containers for both Device and Vehicle services using **Podman Pods** for grouping and modular Taskfiles.

## 📁 Structure

```
devtool/
├── Taskfile.yml                          # Main orchestration file
├── TemporalDevice.yaml                   # Device-specific pod management
├── TemporalVehicle.yaml                  # Vehicle-specific pod management
└── README.md                             # This file
```

## Prerequisites

- [Podman](https://podman.io/) installed
- [Task](https://taskfile.dev/) installed
- **No docker-compose required** - Uses native Podman pods

## 🔌 Port Mappings

### Device Services (workflow-device)
- PostgreSQL: `localhost:5433` → container:5432
- Temporal Server: `localhost:7234` → container:7233
- Temporal UI: `localhost:8081` → container:8080

### Vehicle Services (workflow-vehicle)
- PostgreSQL: `localhost:5434` → container:5432
- Temporal Server: `localhost:7235` → container:7233 *(adjusted to avoid conflict)*
- Temporal UI: `localhost:8082` → container:8080 *(adjusted to avoid conflict)*

## 📦 Pod Organization

All containers are organized using **Podman Pods** for grouping:
- **Device Pod**: `temporal-device-pod` containing all device-related containers
- **Vehicle Pod**: `temporal-vehicle-pod` containing all vehicle-related containers
- **Container Naming**: Uses `temporal-device-*` and `temporal-vehicle-*` prefixes
- **Network Isolation**: Each pod has its own network namespace
- **Port Management**: Clean port mapping per pod (5433/7234/8081 for device, 5434/7235/8082 for vehicle)

## 🚀 Quick Start Commands

### Main Orchestration Commands

```bash
# Quick shortcuts
task device           # 🚗 Start Device Temporal workflow
task vehicle          # 🚙 Start Vehicle Temporal workflow

# Combined operations
task up               # 🚀 Start both Device and Vehicle workflows
task down             # 🛑 Stop both workflows
task restart          # 🔄 Restart both workflows

# Development environment
task dev:setup        # 🛠️  Complete development setup
task dev:reset        # 🔄 Reset environment (force-clean + setup)
```

### Device-Specific Commands

```bash
# Core operations
task device:up         # Start Device Temporal pod with all services
task device:down       # Stop and remove Device Temporal pod
task device:restart    # Restart Device pod
task device:logs       # Show Temporal server logs
task device:logs:all   # Show logs from all containers in pod

# Management
task device:status     # Show pod and container status
task device:health     # Check pod health
task device:clean      # Clean up pod and containers
task device:ports      # Show port mappings

# Temporal operations
task device:workflow:list              # List workflows
task device:workflow:describe WORKFLOW_ID=<id>  # Describe workflow
task device:namespace:list             # List namespaces

# Shell access
task device:shell:temporal    # Access admin tools container
task device:shell:postgres    # Connect to PostgreSQL
```

### Vehicle-Specific Commands

```bash
# Core operations
task vehicle:up         # Start Vehicle Temporal pod with all services
task vehicle:down       # Stop and remove Vehicle Temporal pod
task vehicle:restart    # Restart Vehicle pod
task vehicle:logs       # Show Temporal server logs
task vehicle:logs:all   # Show logs from all containers in pod

# Management
task vehicle:status     # Show pod and container status
task vehicle:health     # Check pod health
task vehicle:clean      # Clean up pod and containers
task vehicle:ports      # Show port mappings

# Temporal operations
task vehicle:workflow:list              # List workflows
task vehicle:workflow:describe WORKFLOW_ID=<id>  # Describe workflow
task vehicle:namespace:list             # List namespaces

# Shell access
task vehicle:shell:temporal    # Access admin tools container
task vehicle:shell:postgres    # Connect to PostgreSQL
```

### Global Utility Commands

```bash
task status           # 📊 Show all pod and container status
task health           # 🏥 Check all pod health
task logs             # 📋 Show logs from all services
task ports            # 🔌 Show all port mappings
task clean            # 🧹 Clean up all pods and containers
```

## 💡 Usage Examples

### 1. Complete Development Setup
```bash
cd devtool
task dev:setup
# This will start both services and show access URLs
```

### 2. Work with Device Workflows Only
```bash
cd devtool
task device:up
task device:workflow:list
task device:logs:all
```

### 3. Work with Vehicle Workflows Only
```bash
cd devtool
task vehicle:up
task vehicle:workflow:list
task vehicle:logs:all
```

### 4. Monitor All Services
```bash
cd devtool
task status
task health
task ports
```

### 5. Troubleshooting
```bash
cd devtool
task logs                    # View all logs
task device:shell:temporal   # Debug device workflows
task vehicle:shell:postgres  # Check vehicle database
```

### 6. Clean Reset
```bash
cd devtool
task dev:reset
# This will force-clean everything and set up fresh
```

## 🔧 Pod-Based Architecture

The system uses **Podman Pods** for clean container grouping and management:

- **Device Pod**: `temporal-device-pod` with dedicated ports (5433, 7234, 8081)
- **Vehicle Pod**: `temporal-vehicle-pod` with dedicated ports (5434, 7235, 8082)
- **Container Naming**: Proper prefixes ensure no naming conflicts
- **Network Isolation**: Each pod has its own network namespace
- **No Docker Compose**: Pure Podman solution using native pod functionality

All pod management is handled within the Taskfiles - no external configuration files needed.

## 🏗️ Architecture

### Modular Design
- **Main Taskfile**: Orchestrates and provides shortcuts
- **TemporalDevice.yaml**: Complete Device pod management using Podman pods
- **TemporalVehicle.yaml**: Complete Vehicle pod management using Podman pods
- **Self-Contained**: All pod creation and management handled within Taskfiles

### Pod Organization
- All containers grouped in logical pods
- Each pod contains: PostgreSQL, Elasticsearch, Temporal Server, Temporal UI, Admin Tools
- Network isolation per pod
- Consistent naming conventions for easy management

## 🔍 Troubleshooting

| Issue | Command | Description |
|-------|---------|-------------|
| Port conflicts | `task ports` | Check current port usage |
| Pod health | `task health` | Verify all pods are running |
| Pod/Container status | `task status` | See pod and container states |
| View logs | `task logs` | Check service logs |
| Clean start | `task clean && task up` | Complete environment reset |
| Database issues | `task device:shell:postgres` | Direct database access |
| Workflow debugging | `task device:shell:temporal` | Access Temporal CLI tools |

## 🌐 Access URLs

After running `task up` or `task dev:setup`:

- **Device Temporal UI**: http://localhost:8081
- **Vehicle Temporal UI**: http://localhost:8082
- **Device PostgreSQL**: localhost:5433
- **Vehicle PostgreSQL**: localhost:5434
- **Device Temporal Server**: localhost:7234
- **Vehicle Temporal Server**: localhost:7235
