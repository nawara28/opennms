/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.provision.snmp;

import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.model.HwEntityAttributeType;
import org.opennms.netmgt.model.OnmsHwEntity;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpRowResult;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * The Class EntityPhysicalTableRow.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class EntityPhysicalTableRow extends SnmpRowResult {

    /** The Constant entPhysicalDescr. */
    public final static SnmpObjId entPhysicalDescr = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.2");

    /** The Constant entPhysicalVendorType. */
    public final static SnmpObjId entPhysicalVendorType = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.3");

    /** The Constant entPhysicalContainedIn. */
    public final static SnmpObjId entPhysicalContainedIn = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.4");

    /** The Constant entPhysicalClass. */
    public final static SnmpObjId entPhysicalClass = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.5");

    /** The Constant entPhysicalParentRelPos. */
    public final static SnmpObjId entPhysicalParentRelPos = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.6");

    /** The Constant entPhysicalName. */
    public final static SnmpObjId entPhysicalName = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.7");

    /** The Constant entPhysicalHardwareRev. */
    public final static SnmpObjId entPhysicalHardwareRev = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.8");

    /** The Constant entPhysicalFirmwareRev. */
    public final static SnmpObjId entPhysicalFirmwareRev = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.9");

    /** The Constant entPhysicalSoftwareRev. */
    public final static SnmpObjId entPhysicalSoftwareRev = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.10");

    /** The Constant entPhysicalSerialNum. */
    public final static SnmpObjId entPhysicalSerialNum = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.11");

    /** The Constant entPhysicalMfgName. */
    public final static SnmpObjId entPhysicalMfgName = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.12");

    /** The Constant entPhysicalModelName. */
    public final static SnmpObjId entPhysicalModelName = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.13");

    /** The Constant entPhysicalAlias. */
    public final static SnmpObjId entPhysicalAlias = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.14");

    /** The Constant entPhysicalAssetID. */
    public final static SnmpObjId entPhysicalAssetID = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.15");

    /** The Constant entPhysicalIsFRU. */
    public final static SnmpObjId entPhysicalIsFRU = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.16");

    /** The Constant entPhysicalMfgDate. */
    public final static SnmpObjId entPhysicalMfgDate = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.17"); // FIXME Not parsed

    /** The Constant entPhysicalUris. */
    public final static SnmpObjId entPhysicalUris = SnmpObjId.get(".1.3.6.1.2.1.47.1.1.1.1.18");

    /** The Constant ELEMENTS. */
    public static final SnmpObjId[] ELEMENTS = new SnmpObjId[] {
        entPhysicalDescr,
        entPhysicalVendorType,
        entPhysicalContainedIn,
        entPhysicalClass,
        entPhysicalParentRelPos,
        entPhysicalName,
        entPhysicalHardwareRev,
        entPhysicalFirmwareRev,
        entPhysicalSoftwareRev,
        entPhysicalSerialNum,
        entPhysicalMfgName,
        entPhysicalModelName,
        entPhysicalAlias,
        entPhysicalAssetID,
        entPhysicalIsFRU,
        entPhysicalMfgDate, // FIXME: Currently Ignored.
        entPhysicalUris
    };

    /** The Constant CLASSES. */
    public static final String[] CLASSES = new String[] {
        null,
        "other",
        "unknown",
        "chassis",
        "backplane",
        "container",
        "powerSupply",
        "fan",
        "sensor",
        "module",
        "port",
        "stack"
    };

    /** The vendor attributes. */
    private Map<SnmpObjId, HwEntityAttributeType> vendorAttributes = new HashMap<SnmpObjId, HwEntityAttributeType>();

    /**
     * The Constructor.
     *
     * @param vendorAttributes the vendor attributes
     * @param columnCount the column count
     * @param instance the instance
     */
    public EntityPhysicalTableRow(Map<SnmpObjId, HwEntityAttributeType> vendorAttributes, int columnCount, SnmpInstId instance) {
        super(columnCount, instance);
        this.vendorAttributes = vendorAttributes;
    }

    /**
     * Gets the entity physical index.
     *
     * @return the entity physical index
     */
    public int getEntPhysicalIndex() {
        return getInstance().getLastSubId();
    }

    /**
     * Gets the hardware entity.
     *
     * @return the hardware entity
     */
    public OnmsHwEntity getOnmsHwEntity() {
        SnmpValue v = null;
        final OnmsHwEntity entity = new OnmsHwEntity();
        entity.setEntPhysicalIndex(getEntPhysicalIndex());
        v = getValue(entPhysicalDescr);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalDescr(v.toDisplayString());
        v = getValue(entPhysicalVendorType);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalVendorType(v.toDisplayString());
        v = getValue(entPhysicalContainedIn);
        if (v != null)
            entity.setEntPhysicalContainedIn(v.toInt());
        v = getValue(entPhysicalClass);
        if (v != null)
            entity.setEntPhysicalClass(CLASSES[v.toInt()]);
        v = getValue(entPhysicalParentRelPos);
        if (v != null)
            entity.setEntPhysicalParentRelPos(v.toInt());
        v = getValue(entPhysicalName);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalName(v.toDisplayString());
        v = getValue(entPhysicalHardwareRev);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalHardwareRev(v.toDisplayString());
        v = getValue(entPhysicalFirmwareRev);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalFirmwareRev(v.toDisplayString());
        v = getValue(entPhysicalSoftwareRev);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalSoftwareRev(v.toDisplayString());
        v = getValue(entPhysicalSerialNum);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalSerialNum(v.toDisplayString());
        v = getValue(entPhysicalMfgName);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalMfgName(v.toDisplayString());
        v = getValue(entPhysicalModelName);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalModelName(v.toDisplayString());
        v = getValue(entPhysicalAlias);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalAlias(v.toDisplayString());
        v = getValue(entPhysicalAssetID);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalAssetID(v.toDisplayString());
        v = getValue(entPhysicalIsFRU);
        if (v != null)
            entity.setEntPhysicalIsFRU(v.toInt() == 1 ? true : false);
        v = getValue(entPhysicalUris);
        if (v != null && !v.toDisplayString().trim().equals(""))
            entity.setEntPhysicalUris(v.toDisplayString());
        if (vendorAttributes != null && vendorAttributes.size() > 0) {
            for (Map.Entry<SnmpObjId, HwEntityAttributeType> entry : vendorAttributes.entrySet()) {
                v = getValue(entry.getKey());
                if (v != null && !v.toDisplayString().trim().equals("")) {
                    entity.addAttribute(entry.getValue(), v.toDisplayString());
                }                
            }
        }
        return entity;
    }
}   