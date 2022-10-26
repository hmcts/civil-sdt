/* Copyrights and Licenses
 *
 * Copyright (c) 2013 by the Ministry of Justice. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * - All advertising materials mentioning features or use of this software must display the
 * following acknowledgment: "This product includes Money Claims OnLine."
 * - Products derived from this software may not be called "Money Claims OnLine" nor may
 * "Money Claims OnLine" appear in their names without prior written permission of the
 * Ministry of Justice.
 * - Redistributions of any form whatsoever must retain the following acknowledgment: "This
 * product includes Money Claims OnLine."
 * This software is provided "as is" and any expressed or implied warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the Ministry of Justice or its contributors be liable for any
 * direct, indirect, incidental, special, exemplary, or consequential damages (including, but
 * not limited to, procurement of substitute goods or services; loss of use, data, or profits;
 * or business interruption). However caused any on any theory of liability, whether in contract,
 * strict liability, or tort (including negligence or otherwise) arising in any way out of the use of this
 * software, even if advised of the possibility of such damage.
 *
 * $Id: ClaimXsdTest.java 16414 2013-05-29 10:56:45Z agarwals $
 * $LastChangedRevision: 16414 $
 * $LastChangedDate: 2013-05-29 11:56:45 +0100 (Wed, 29 May 2013) $
 * $LastChangedBy: holmessm $ */
package uk.gov.moj.sdt.validators.validation;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import uk.gov.moj.sdt.utils.AbstractSdtUnitTestBase;
import uk.gov.moj.sdt.validators.DateValidator;

/**
 * Test class for DateValidation.
 *
 * @author d301488
 */
public class DateValidatorTest extends AbstractSdtUnitTestBase {

    /**
     * Test: isDateWitinRange
     * <p>
     * Expected: TRUE as the date given is within the range.
     */
    @Test
    public void testIsDateWitinRangeTrue() {

        final LocalDate dateToTest = new LocalDate();

        final LocalDate startDate = new LocalDate().minusMonths(12);
        final LocalDate endDate = new LocalDate().plusMonths(12);

        Assert.assertTrue("The date is no longer considered as witin range.",
                DateValidator.isDateWitinRange(dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateWitinRange
     * <p>
     * Expected: TRUE - as the date given is on the same day as the end date.
     */
    @Test
    public void testIsDateWitinRangeSameDate() {
        final LocalDate dateToTest = new LocalDate();

        final LocalDate startDate = new LocalDate().minusMonths(12);

        Assert.assertTrue("The date is no longer considered as witin range.",
                DateValidator.isDateWitinRange(dateToTest, startDate, dateToTest));
    }

    /**
     * Test: isDateWitinRange
     * <p>
     * Outside Range - date given is past end date.
     * <p>
     * Expected: FALSE as the date given is NOT within the range.
     */
    @Test
    public void testIsDateWitinRangeFalseEnd() {
        final LocalDate dateToTest = new LocalDate().plusMonths(12);

        final LocalDate startDate = new LocalDate();
        final LocalDate endDate = new LocalDate().minusMonths(12);

        Assert.assertFalse("The test date no longer is outside the end of the given range.",
                DateValidator.isDateWitinRange(dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateWitinRange
     * <p>
     * Outside Range - date given is before start date.
     * <p>
     * Expected: Method returns FALSE as the date given is NOT within the range.
     */
    @Test
    public void testIsDateWitinRangeFalseStart() {
        final LocalDate dateToTest = new LocalDate().minusMonths(12);

        final LocalDate startDate = new LocalDate().plusMonths(12);
        final LocalDate endDate = new LocalDate();

        Assert.assertFalse("The test date no longer is before the start of the given range.",
                DateValidator.isDateWitinRange(dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateBefore
     * <p>
     * Expected: TRUE as the date given is BEFORE the given date.
     */
    @Test
    public void testIsDateBeforeTrue() {
        final LocalDate dateToTest = new LocalDate();

        final LocalDate endDate = new LocalDate().plusMonths(12);

        Assert.assertTrue("The test date is no longer considered to be after the given date.",
                DateValidator.isDateBefore(dateToTest, endDate));
    }

    /**
     * Test: isDateBefore
     * <p>
     * Expected: FALSE as the date given is AFTER the end date.
     */
    @Test
    public void testIsDateBeforeFalse() {
        final LocalDate dateToTest = new LocalDate().plusMonths(12);

        final LocalDate endDate = new LocalDate();

        Assert.assertFalse("The test date is now considered to be before or on the same day the given date.",
                DateValidator.isDateBefore(dateToTest, endDate));
    }

    /**
     * Test: isDateAfter
     * <p>
     * Expected: TRUE as the date given is AFTER the given date.
     */
    @Test
    public void testIsDateAfterTrue() {
        final LocalDate dateToTest = new LocalDate().plusMonths(12);

        final LocalDate endDate = new LocalDate();

        Assert.assertTrue("The test date is now considered to be before the given date.",
                DateValidator.isDateAfter(dateToTest, endDate));
    }

    /**
     * Test: isDateAfter
     * <p>
     * Expected: FALSE as the date given is BEFORE the range.
     */
    @Test
    public void testIsDateAfterFalse() {
        final LocalDate dateToTest = new LocalDate();

        final LocalDate endDate = new LocalDate().plusMonths(12);

        Assert.assertFalse("The test date is now considered to be after or on the same day the given date.",
                DateValidator.isDateAfter(dateToTest, endDate));
    }

    /**
     * Test: isDateWitinXDays
     * <p>
     * Expected: TRUE the date given is within 10 days of today.
     */
    @Test
    public void testIsDateWitinXDaysWITHIN() {
        final LocalDate dateToTest = new LocalDate().minusDays(5);

        Assert.assertTrue("The date is now considered to be further away than the given number of days.",
                DateValidator.isDateWitinLastXDays(dateToTest, 10));
    }

    /**
     * Test: isDateWitinXDays
     * <p>
     * Expected: TRUE the date given is within 10 days of today.
     */
    @Test
    public void testIsDateWitinXDaysONBOUNDARY() {
        final LocalDate dateToTest = new LocalDate().minusDays(10);

        Assert.assertTrue("The date is now considered to be further away than the given number of days.",
                DateValidator.isDateWitinLastXDays(dateToTest, 10));
    }

    /**
     * Test: isDateWitinXDays
     * <p>
     * Expected: TRUE the date given is within 10 days of today.
     */
    @Test
    public void testIsDateWitinXDaysOUTSIDE() {
        final LocalDate dateToTest = new LocalDate().minusDays(15);

        Assert.assertFalse("The date is now considered to be within the given number of days.",
                DateValidator.isDateWitinLastXDays(dateToTest, 10));
    }
}
