/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.creditApp
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:17 PM
 */

package org.rmj.guanzongroup.onlinecreditapplication.Model;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SpouseResidenceInfoModelTest extends TestCase {
    private SpouseResidenceInfoModel infoModel;
    private final String HOUSENOX = "155";
    private final String ADDRESS1 = "Lot 25";
    private final String ADDRESS2 = "Perez Boulevard";

    @Before
    public void setUp() {
        infoModel = new SpouseResidenceInfoModel();

        infoModel.setLandmark("Guanzon Bldg.");
        infoModel.setHouseNox(HOUSENOX);
        infoModel.setAddress1(ADDRESS1);
        infoModel.setAddress2(ADDRESS2);
        infoModel.setProvince("2132");
        infoModel.setTown("21318");
        infoModel.setBarangay("21321");
    }

    @Test
    public void test_isSpouseResidenceInfoValid() {
        assertTrue(infoModel.isSpouseResidenceInfoValid());
    }

    @Test
    public void test_getLandmark() {
        assertEquals("Guanzon Bldg.", infoModel.getLandmark());
    }

    @Test
    public void test_getHouseNox() {
        assertEquals(HOUSENOX, infoModel.getHouseNox());
    }

    @Test
    public void test_getAddress1() {
        assertEquals(ADDRESS1, infoModel.getAddress1());
    }

    @Test
    public void test_getAddress2() {
        assertEquals(ADDRESS2, infoModel.getAddress2());
    }

    @Test
    public void test_getProvince() {
        assertEquals("2132", infoModel.getProvince());
    }

    @Test
    public void test_getTown() {
        assertEquals("21318", infoModel.getTown());
    }

    @Test
    public void test_getBarangay() {
        assertEquals("21321", infoModel.getBarangay());
    }
}
