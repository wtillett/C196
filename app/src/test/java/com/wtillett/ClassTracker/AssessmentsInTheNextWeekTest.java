package com.wtillett.ClassTracker;

import com.wtillett.ClassTracker.database.Assessment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.wtillett.ClassTracker.AssessmentsInTheNextWeekActivity.isThisWeek;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AssessmentsInTheNextWeekTest {

    @Test
    public void isYesterday() {
        Assessment yesterday = new Assessment(
                "Yesterday", LocalDate.now().minusDays(1), true
        );
        assertFalse(isThisWeek(yesterday));
    }

    @Test
    public void isToday() {
        Assessment today = new Assessment(
                "Today", LocalDate.now(), true
        );
        assertTrue(isThisWeek(today));
    }

    @Test
    public void isOneWeek() {
        Assessment oneWeek = new Assessment(
                "One Week", LocalDate.now().plusDays(7), true
        );
        assertTrue(isThisWeek(oneWeek));
    }

    @Test
    public void isOneWeekPlusOneDay() {
        Assessment oneWeekPlusOneDay = new Assessment(
                "One Week Plus One Day", LocalDate.now().plusDays(8), true
        );
        assertFalse(isThisWeek(oneWeekPlusOneDay));
    }
}