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

import junit.framework.TestCase;

import org.joda.time.LocalDateTime;
import org.junit.Assert;

/**
 * Test class for DateValidation.
 * 
 * @author d301488
 * 
 */
public class DateValidatorTest extends TestCase
{

    /**
     * Test: isDateWitinRange
     * 
     * Expected: TRUE as the date given is within the range.
     */
    public void testIsDateWitinRangeTrue ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ();

        final LocalDateTime startDate = new LocalDateTime ().minusMonths (12);
        final LocalDateTime endDate = new LocalDateTime ().plusMonths (12);

        Assert.assertTrue ("The date is no longer considered as witin range.",
                dateValidator.isDateWitinRange (dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateWitinRange
     * 
     * Expected: TRUE - as the date given is on the same day as the end date.
     */
    public void testIsDateWitinRangeSameDate ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ();

        final LocalDateTime startDate = new LocalDateTime ().minusMonths (12);

        Assert.assertTrue ("The date is no longer considered as witin range.",
                dateValidator.isDateWitinRange (dateToTest, startDate, dateToTest));
    }

    /**
     * Test: isDateWitinRange
     * 
     * Outside Range - date given is past end date.
     * 
     * Expected: FALSE as the date given is NOT within the range.
     */
    public void testIsDateWitinRangeFalseEnd ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().plusMonths (12);

        final LocalDateTime startDate = new LocalDateTime ();
        final LocalDateTime endDate = new LocalDateTime ().minusMonths (12);

        Assert.assertFalse ("The test date no longer is outside the end of the given range.",
                dateValidator.isDateWitinRange (dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateWitinRange
     * 
     * Outside Range - date given is before start date.
     * 
     * Expected: Method returns FALSE as the date given is NOT within the range.
     */
    public void testIsDateWitinRangeFalseStart ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().minusMonths (12);

        final LocalDateTime startDate = new LocalDateTime ().plusMonths (12);
        final LocalDateTime endDate = new LocalDateTime ();

        Assert.assertFalse ("The test date no longer is before the start of the given range.",
                dateValidator.isDateWitinRange (dateToTest, startDate, endDate));
    }

    /**
     * Test: isDateBefore
     * 
     * Expected: TRUE as the date given is BEFORE the given date.
     */
    public void testIsDateBeforeTrue ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ();

        final LocalDateTime endDate = new LocalDateTime ().plusMonths (12);

        Assert.assertTrue ("The test date is no longer considered to be after the given date.",
                dateValidator.isDateBefore (dateToTest, endDate));
    }

    /**
     * Test: isDateBefore
     * 
     * Expected: FALSE as the date given is AFTER the end date.
     */
    public void testIsDateBeforeFalse ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().plusMonths (12);

        final LocalDateTime endDate = new LocalDateTime ();

        Assert.assertFalse ("The test date is now considered to be before or on the same day the given date.",
                dateValidator.isDateBefore (dateToTest, endDate));
    }

    /**
     * Test: isDateAfter
     * 
     * Expected: TRUE as the date given is AFTER the given date.
     */
    public void testIsDateAfterTrue ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().plusMonths (12);

        final LocalDateTime endDate = new LocalDateTime ();

        Assert.assertTrue ("The test date is now considered to be before the given date.",
                dateValidator.isDateAfter (dateToTest, endDate));
    }

    /**
     * Test: isDateAfter
     * 
     * Expected: FALSE as the date given is BEFORE the range.
     */
    public void testIsDateAfterFalse ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ();

        final LocalDateTime endDate = new LocalDateTime ().plusMonths (12);

        Assert.assertFalse ("The test date is now considered to be after or on the same day the given date.",
                dateValidator.isDateAfter (dateToTest, endDate));
    }

    /**
     * Test: isDateWitinXDays
     * 
     * Expected: TRUE the date given is within 10 days of today.
     */
    public void testIsDateWitinXDaysWITHIN ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().minusDays (5);

        Assert.assertTrue ("The date is now considered to be further away than the given number of days.",
                dateValidator.isDateWitinLastXDays (dateToTest, 10));
    }

    /**
     * Test: isDateWitinXDays
     * 
     * Expected: TRUE the date given is within 10 days of today.
     */
    public void testIsDateWitinXDaysONBOUNDARY ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().minusDays (10);

        Assert.assertTrue ("The date is now considered to be further away than the given number of days.",
                dateValidator.isDateWitinLastXDays (dateToTest, 10));
    }

    /**
     * Test: isDateWitinXDays
     * 
     * Expected: TRUE the date given is within 10 days of today.
     */
    public void testIsDateWitinXDaysOUTSIDE ()
    {
        final IDateValidator dateValidator = new DateValidator ();

        final LocalDateTime dateToTest = new LocalDateTime ().minusDays (15);

        Assert.assertFalse ("The date is now considered to be within the given number of days.",
                dateValidator.isDateWitinLastXDays (dateToTest, 10));
    }
}
