package ru.nsu.fit.g16205.ivanishkin.volumeRendering;

import ru.nsu.fit.g16205.ivanishkin.utils.Pair;
import ru.nsu.fit.g16205.ivanishkin.utils.Point3;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    public final List<Pair<Integer, Double>> dotsAbsorption;
    public final List<Pair<Integer, Integer>> dotsRedEmission;
    public final List<Pair<Integer, Integer>> dotsGreenEmission;
    public final List<Pair<Integer, Integer>> dotsBlueEmission;
    public final Map<Point3, Double> charges;


    private Config(List<Pair<Integer, Double>> dotsAbsorption,
                   List<Pair<Integer, Integer>> dotsRedEmission,
                   List<Pair<Integer, Integer>> dotsGreenEmission,
                   List<Pair<Integer, Integer>> dotsBlueEmission,
                   Map<Point3, Double> charges) {
        this.dotsAbsorption = dotsAbsorption;
        this.dotsRedEmission = dotsRedEmission;
        this.dotsGreenEmission = dotsGreenEmission;
        this.dotsBlueEmission = dotsBlueEmission;
        this.charges = charges;
    }

    public static Config from(final Reader reader) {
        BufferedReader r = new BufferedReader(reader);
        int count;
        List<Pair<Integer, Double>> dotsAbs = new ArrayList<>();
        List<Pair<Integer, Integer>> dotsR = new ArrayList<>();
        List<Pair<Integer, Integer>> dotsG = new ArrayList<>();
        List<Pair<Integer, Integer>> dotsB = new ArrayList<>();
        Map<Point3, Double> dotsCharges = new HashMap<>();
        try {
            List<String> lines = r.lines()
                    .map(it -> it.substring(0, it.contains("//") ? it.indexOf("//") : it.length()))
                    .map(String::trim)
                    .filter(it -> !it.isEmpty())
                    .collect(Collectors.toList());

            List<String[]> absorption = readCountGetSplitedStrings(lines);
            lines = lines.subList(absorption.size() + 1, lines.size());

            List<String[]> emission = readCountGetSplitedStrings(lines);
            lines = lines.subList(emission.size() + 1, lines.size());

            List<String[]> charges = readCountGetSplitedStrings(lines);

            for (String[] dot : absorption) {
                dotsAbs.add(new Pair<>(Integer.parseInt(dot[0]), Double.parseDouble(dot[1])));
            }
            for (String[] dot : emission) {
                int x = Integer.parseInt(dot[0]);
                dotsR.add(new Pair<>(x, Integer.parseInt(dot[1])));
                dotsG.add(new Pair<>(x, Integer.parseInt(dot[2])));
                dotsB.add(new Pair<>(x, Integer.parseInt(dot[3])));
            }
            for (String[] dot : charges) {
                dotsCharges.put(
                        new Point3(
                                Double.parseDouble(dot[0]),
                                Double.parseDouble(dot[1]),
                                Double.parseDouble(dot[2])),
                        Double.parseDouble(dot[3]));
            }
            return new Config(dotsAbs, dotsR, dotsG, dotsB, dotsCharges);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while reading config", e);
        }
    }

    private static List<String[]> readCountGetSplitedStrings(List<String> lines) {
        int limit = Integer.parseInt(lines.get(0));
        lines = lines.subList(1, lines.size());
        return lines.stream()
                .limit(limit)
                .map(it -> it.split(" "))
                .collect(Collectors.toList());
    }
}
