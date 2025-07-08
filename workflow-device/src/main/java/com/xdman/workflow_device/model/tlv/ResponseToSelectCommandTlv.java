package com.xdman.workflow_device.model.tlv;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlv;
import com.payneteasy.tlv.BerTlvBuilder;
import com.payneteasy.tlv.BerTlvParser;
import com.payneteasy.tlv.BerTlvs;
import com.payneteasy.tlv.HexUtil;

import com.xdman.workflow_device.model.TlvBase;
import lombok.Getter;
import lombok.Setter;

/**
 * Response to SELECT Command TLV format according to Table 5-3:
 * <p>
 * Tag    Length     Description                                          Field
 * 5Ah    2 × n      Supported vehicle-owner device framework versions    Mandatory
 * (V-OD-FWdeviceList). (ver.high | ver.low)
 * 5Ch    2 × m      Supported Digital Key applet protocol versions       Mandatory
 * (ver.high | ver.low)
 * D4h    1          Pairing Mode:                                       Mandatory
 * 00h = not in pairing mode
 * 02h = pairing mode started, and pairing password entered
 */
@Setter
@Getter
public class ResponseToSelectCommandTlv extends TlvBase {
  // Common format constants
  private static final String SUCCESS_TRAILER = "9000";

  // Pairing mode constants
  public static final int PAIRING_MODE_NOT_IN_PAIRING = 0x00;
  public static final int PAIRING_MODE_STARTED_WITH_PASSWORD = 0x02;

  // Fields
  private byte[] frameworkVersions;  // Array of version pairs (high, low)
  private byte[] protocolVersions;   // Array of version pairs (high, low)
  private Integer pairingMode;       // 0x00 or 0x02

  private enum Tags {
    FRAMEWORK_VERSIONS("5A"),
    PROTOCOL_VERSIONS("5C"),
    PAIRING_MODE("D4");

    public final BerTag tag;

    Tags(String hexString) {
      this.tag = new BerTag(HexUtil.parseHex(hexString));
    }
  }

  @Override
  public ResponseToSelectCommandTlv decode(String tlvString) {
    try {
      if (tlvString == null || tlvString.trim().isEmpty()) {
        throw new IllegalArgumentException("TLV string cannot be null or empty");
      }

      // Remove success trailer if present
      String dataHex = tlvString;
      if (dataHex.endsWith(SUCCESS_TRAILER)) {
        dataHex = dataHex.substring(0, dataHex.length() - SUCCESS_TRAILER.length());
      }

      BerTlvParser parser = new BerTlvParser();
      BerTlvs tlvs = parser.parse(HexUtil.parseHex(dataHex));

      // Decode mandatory fields
      BerTlv frameworkVersionsTlv = tlvs.find(Tags.FRAMEWORK_VERSIONS.tag);
      if (frameworkVersionsTlv == null) {
        throw new IllegalArgumentException("Missing mandatory field: Framework versions");
      }
      this.frameworkVersions = frameworkVersionsTlv.getBytesValue();
      validateVersionArray(frameworkVersions, "Framework versions");

      BerTlv protocolVersionsTlv = tlvs.find(Tags.PROTOCOL_VERSIONS.tag);
      if (protocolVersionsTlv == null) {
        throw new IllegalArgumentException("Missing mandatory field: Protocol versions");
      }
      this.protocolVersions = protocolVersionsTlv.getBytesValue();
      validateVersionArray(protocolVersions, "Protocol versions");

      BerTlv pairingModeTlv = tlvs.find(Tags.PAIRING_MODE.tag);
      if (pairingModeTlv == null) {
        throw new IllegalArgumentException("Missing mandatory field: Pairing mode");
      }
      byte[] pairingModeBytes = pairingModeTlv.getBytesValue();
      if (pairingModeBytes.length != 1) {
        throw new IllegalArgumentException("Pairing mode must be exactly 1 byte");
      }
      this.pairingMode = (int) (pairingModeBytes[0] & 0xFF);
      validatePairingMode(pairingMode);

      return this;
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to decode SELECT Response: " + e.getMessage(), e);
    }
  }

  @Override
  public String encode() {
    try {
      validateVersionArray(frameworkVersions, "Framework versions");
      validateVersionArray(protocolVersions, "Protocol versions");
      validatePairingMode(pairingMode);

      BerTlvBuilder builder = new BerTlvBuilder();
      builder.addBytes(Tags.FRAMEWORK_VERSIONS.tag, frameworkVersions);
      builder.addBytes(Tags.PROTOCOL_VERSIONS.tag, protocolVersions);
      // Convert pairingMode to a single byte array before adding
      byte[] pairingModeBytes = new byte[] {pairingMode.byteValue()};
      builder.addBytes(Tags.PAIRING_MODE.tag, pairingModeBytes);

      // Build TLV data and append success trailer
      byte[] tlvBytes = builder.buildArray();
      return HexUtil.toHexString(tlvBytes).toUpperCase() + SUCCESS_TRAILER;
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to encode SELECT Response: " + e.getMessage(), e);
    }
  }



  private void validateVersionArray(byte[] versions, String fieldName) {
    if (versions == null) {
      throw new IllegalArgumentException(fieldName + " cannot be null");
    }
    if (versions.length != 2) {
      throw new IllegalArgumentException(fieldName + " must be exactly 2 bytes (high, low)");
    }
  }

  private void validatePairingMode(Integer mode) {
    if (mode == null) {
      throw new IllegalArgumentException("Pairing mode cannot be null");
    }
    if (mode != PAIRING_MODE_NOT_IN_PAIRING && mode != PAIRING_MODE_STARTED_WITH_PASSWORD) {
      throw new IllegalArgumentException("Invalid pairing mode: " + mode);
    }
  }

  /**
   * Get framework versions formatted as major.minor
   *
   * @return formatted version string
   */
  @JsonIgnore
  public String getFormattedFrameworkVersions() {
    return formatVersion(frameworkVersions);
  }

  /**
   * Get protocol versions formatted as major.minor
   *
   * @return formatted version string
   */
  @JsonIgnore
  public String getFormattedProtocolVersions() {
    return formatVersion(protocolVersions);
  }

  /**
   * Get pairing mode as human-readable string
   *
   * @return formatted pairing mode string
   */
  @JsonIgnore
  public String getFormattedPairingMode() {
    return formatPairingMode(pairingMode);
  }

  private String formatVersion(byte[] version) {
    if (version == null || version.length != 2) {
      return "Unknown";
    }
    return String.format("%d.%d", version[0], version[1]);
  }

  private String formatPairingMode(Integer mode) {
    if (mode == null) {
      return "Unknown";
    }
    return switch (mode) {
      case PAIRING_MODE_NOT_IN_PAIRING -> "Not in pairing mode";
      case PAIRING_MODE_STARTED_WITH_PASSWORD -> "Pairing mode started, password entered";
      default -> "Unknown mode: " + mode;
    };
  }
}
