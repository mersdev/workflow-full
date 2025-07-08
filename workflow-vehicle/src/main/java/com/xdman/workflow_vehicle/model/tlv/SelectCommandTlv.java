package com.xdman.workflow_vehicle.model.tlv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.payneteasy.tlv.HexUtil;
import com.xdman.workflow_vehicle.model.TlvBase;
import lombok.Getter;
import lombok.Setter;

/**
 * SELECT Command TLV format:
 * Command format: 00 A4 04 00 Lc [AID] 00
 */
@Setter
@Getter
public class SelectCommandTlv extends TlvBase {
  // Digital Key Framework AID
  private static final String DIGITAL_KEY_FRAMEWORK_AID = "A0000008094343434B467631";

  /**
   * -- GETTER --
   *  Get the Application Identifier (AID)
   *
   *
   * -- SETTER --
   *  Set the Application Identifier (AID)
   *
   @return the AID value
	* @param aid the AID value to set
   */
  // Fields
  private String aid;  // Application Identifier

  public SelectCommandTlv() {
    this.aid = DIGITAL_KEY_FRAMEWORK_AID;
  }

  @Override
  public SelectCommandTlv decode(String tlvString) {
    try {
      if (tlvString == null || tlvString.trim().isEmpty()) {
        throw new IllegalArgumentException("TLV string cannot be null or empty");
      }

      byte[] allBytes = HexUtil.parseHex(tlvString);
      // Extract data part: after command header (5 bytes) and before trailer (1 byte)
      if (allBytes.length < 6) {
        throw new IllegalArgumentException("Invalid command format: too short");
      }

      // Extract AID from data part (skip 5 byte header, take length from Lc field)
      int dataLength = allBytes[4] & 0xFF; // Lc field
      if (allBytes.length < 5 + dataLength + 1) {
        throw new IllegalArgumentException("Invalid command format: data length mismatch");
      }

      byte[] aidBytes = new byte[dataLength];
      System.arraycopy(allBytes, 5, aidBytes, 0, dataLength);
      this.aid = HexUtil.toHexString(aidBytes).toUpperCase();

      return this;
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to decode SELECT Command: " + e.getMessage(), e);
    }
  }

  @Override
  public String encode() {
    try {
      validateAid(aid);

      // Convert AID to bytes
      byte[] aidBytes = HexUtil.parseHex(aid);

      // Construct full APDU: CLA INS P1 P2 Lc [Data] Le
      byte[] apdu = new byte[5 + aidBytes.length + 1];
      apdu[0] = (byte) 0x00; // CLA
      apdu[1] = (byte) 0xA4; // INS
      apdu[2] = (byte) 0x04; // P1
      apdu[3] = (byte) 0x00; // P2
      apdu[4] = (byte) aidBytes.length; // Lc
      System.arraycopy(aidBytes, 0, apdu, 5, aidBytes.length);
      apdu[5 + aidBytes.length] = (byte) 0x00; // Le

      return HexUtil.toHexString(apdu).toUpperCase();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to encode SELECT Command: " + e.getMessage(), e);
    }
  }



  private void validateAid(String aid) {
    if (aid == null || aid.isEmpty()) {
      throw new IllegalArgumentException("AID cannot be null or empty");
    }
    if (!aid.matches("[0-9A-Fa-f]+")) {
      throw new IllegalArgumentException("AID must be a valid hex string");
    }
    if (aid.length() != DIGITAL_KEY_FRAMEWORK_AID.length()) {
      throw new IllegalArgumentException("Invalid AID length");
    }
  }

  /**
   * Get the AID in formatted hex string
   *
   * @return formatted AID value or "Unknown" if not set
   */
  @JsonIgnore
  public String getFormattedAid() {
    return aid != null ? aid : "Unknown";
  }

  /**
   * Get the default Digital Key Framework AID
   *
   * @return the default Digital Key Framework AID value
   */
  @JsonIgnore
  public static String getDigitalKeyFrameworkAid() {
    return DIGITAL_KEY_FRAMEWORK_AID;
  }
}