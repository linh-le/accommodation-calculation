package org.accommodationcal;

import org.accommodationcal.models.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.assertj.core.api.Assertions.*;


public class MainTest {

    @Test
    public void testConvertToDatesSingle() {
        List<LocalDate> singleDate = Main.convertToDates("11/23");
        assertThat(singleDate).hasSize(1);
        assertThat(singleDate.get(0).getMonth()).isEqualTo(Month.NOVEMBER);
        assertThat(singleDate.get(0).getDayOfMonth()).isEqualTo(23);
    }

    @Test
    public void testConvertToDatesDuration() {
        List<LocalDate> dates = Main.convertToDates("11/23-11/30");
        assertThat(dates).hasSize(8);
        assertThat(dates.toString()).isEqualTo("[2023-11-23, 2023-11-24, 2023-11-25, 2023-11-26, 2023-11-27, 2023-11-28, 2023-11-29, 2023-11-30]");
    }

    @Test
    public void testBuildPersonalInfoSingle(){
        Scanner scanner = new Scanner("Linh\n11/03-11/7");
        List<Person> people = new ArrayList<>();
        Set<LocalDate> allDates = new HashSet<>();
        Main.buildPersonalInfo(scanner, allDates, people);
        assertThat(people).hasSize(1);
        assertThat(allDates).hasSize(5);
        assertThat(people.get(0).getName()).isEqualTo("Linh");
    }

    @Test
    public void testBuildPersonalInfoMultiple(){
        Scanner scanner = new Scanner("Linh\n11/03-11/7\nCharlie\n11/4-11/7\nSandy\n11/3-11/9");
        List<Person> people = new ArrayList<>();
        Set<LocalDate> allDates = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Main.buildPersonalInfo(scanner, allDates, people);
        }
        assertThat(people).hasSize(3);
        assertThat(allDates).hasSize(7);
        assertThat(people.get(2).getName()).isEqualTo("Sandy");
        scanner.close();
    }
}
