package org.accommodationcal;

import org.accommodationcal.models.Person;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Establishing important variables
        // All dates occupied
        List<LocalDate> sortedDates;
        // Cost per night
        double costPerNight;

        System.out.println("Welcome!");
        Scanner reader = new Scanner(System.in);  // Reading from System.in

        // Step 1: Getting names and duration for each renter
        System.out.println("How many people in total will stay together on this trip: ");
        int totalPeople = reader.nextInt(); // Scans the next token of the input as an int.
        Set<LocalDate> allDates = new HashSet<>();
        List<Person> allPeople = new ArrayList<>();
        for (int i = 0; i < totalPeople; i++) {
            buildPersonalInfo(reader, allDates, allPeople);
        }

        // Step 2: Get cost information
        System.out.println("How much does this accommodation cost in total? ");
        double totalCost = reader.nextDouble();
        costPerNight = totalCost / allDates.size();

        // Step 3: Get type of cost share - equal or percentage.
        sortedDates = allDates.stream().sorted().collect(Collectors.toList());
        // take into account who is staying on which date
        Map<LocalDate, List<Person>> datesMap = sortedDates.stream().collect(Collectors.toMap(date -> date, date -> {
            List<Person> peopleOnSameDate = new ArrayList<>();
            for (Person p : allPeople) {
                if (p.getDatesOfAccommodation().contains(date)) peopleOnSameDate.add(p);
            }
            return peopleOnSameDate;
        }));

        // can add better ways to detect boolean input
        System.out.println("Will everyone split the place equally the entire time? Type TRUE or FALSE");

        boolean isEqual = reader.nextBoolean();

        if (isEqual) {
            for (Map.Entry<LocalDate, List<Person>> entry : datesMap.entrySet()) {
                List<Person> peopleEachDay = entry.getValue();
                for (Person p : peopleEachDay) {
                    p.setRentMultiplier(p.getRentMultiplier() + 1.0 / peopleEachDay.size());
                }
            }
        }
        // TODO If not split equal

        //once finished
        reader.close();

        // Step 4: Share results
        String line = String.format("%d people are staying from %s to %s: %s \n", allPeople.size(), sortedDates.get(0), sortedDates.get(sortedDates.size() - 1), allPeople.stream().map(Person::getName).collect(Collectors.toList()));
        System.out.println(line);
        for (Person p : allPeople) {
            line = String.format("%s is staying on these dates %s and pays %.2f \n", p.getName(), p.getDatesOfAccommodation(), Math.ceil(p.getRentMultiplier() * costPerNight * 100) / 100);
            System.out.printf(line);
        }

    }

    protected static void buildPersonalInfo(Scanner reader, Set<LocalDate> totalSortedDates, List<Person> allPeople) {
        System.out.println("Enter the name of a renter: ");
        String name = reader.next();
        System.out.println("What are the NIGHTS this person is staying for? Enter in the format of MM/DD-MM/DD");
        String dates = reader.next();
        List<LocalDate> durationForThisPerson = convertToDates(dates);
        allPeople.add(Person.builder().name(name).datesOfAccommodation(durationForThisPerson).build());
        totalSortedDates.addAll(durationForThisPerson);
    }

    protected static List<LocalDate> convertToDates(String dates) {
        if (dates.contains("-")) {
            String[] duration = dates.split("-");
            // there should only be FROM and TO dates
            assert duration.length == 2;
            LocalDate fromDate = extractDate(duration[0].split("/"));
            LocalDate toDate = extractDate(duration[1].split("/"));
            List<LocalDate> allDates;
            allDates = fromDate.datesUntil(toDate).collect(Collectors.toList());
            // above is exclusive of end date
            allDates.add(toDate);
            return allDates;
        } else {
            // single date
            String[] split = dates.split("/");
            LocalDate singleDate = extractDate(split);
            return List.of(singleDate);
        }

        // TODO not yet taken care of - single date AND a duration
    }

    private static LocalDate extractDate(String[] split) {
        return LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}