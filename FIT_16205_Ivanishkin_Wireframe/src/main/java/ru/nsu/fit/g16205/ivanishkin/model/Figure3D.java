package ru.nsu.fit.g16205.ivanishkin.model;

import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslator;
import ru.nsu.fit.g16205.ivanishkin.geom.PointTranslatorBuilder;
import ru.nsu.fit.g16205.ivanishkin.geom.UniformPoint3D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Figure3D {
    private static int VISIBLE_SPLINES_N = 5;
    private static int INVISIBLE_SPLINES_K = 5;
    private static int PARALLELS_M = 18;

    // todo: doesn't work, fix
    private static double ANGLE_START_C = 0;
    private static double ANGLE_END_D = 2 * Math.PI;

    private final Spline spline;
    private UniformPoint3D center;

    private List<List<UniformPoint3D>> splinesPoints = new ArrayList<>();
    private List<List<UniformPoint3D>> parallelsPoints = new ArrayList<>();

    public Figure3D(Spline spline, UniformPoint3D center) {
        this.spline = spline;
        this.center = center;
        initFigure();
    }

    private void initFigure() {
        // todo: check angle
        // todo: add invisible
        double angleStep = 2 * Math.PI / (int) (VISIBLE_SPLINES_N * (ANGLE_END_D - ANGLE_START_C) * 0.5 / Math.PI);
        PointTranslator firstRotator = new PointTranslatorBuilder().rotateOverX(ANGLE_START_C).build();

        List<UniformPoint3D> oneSpline = spline.getSubsplinePoints().stream()
                .map(p -> new UniformPoint3D(p.getX(), p.getY(), 0, 1))
                .map(firstRotator::translate)
                .collect(Collectors.toList());

        for (int i = 0; i < VISIBLE_SPLINES_N + 1; i++) {
            PointTranslator t = new PointTranslatorBuilder()
                    .rotateOverX(angleStep * i)
                    .shift(center.getX(), center.getY(), center.getZ())
                    .build();
            splinesPoints.add(oneSpline.stream()
                    .map(t::translate)
                    .collect(Collectors.toList())
            );
        }

        int[] parallelsIndices = spline.getParallelsPoints(VISIBLE_SPLINES_N);

        double angleForInvisible = angleStep / INVISIBLE_SPLINES_K;
        for (int pIndex : parallelsIndices) {
            List<UniformPoint3D> parallel = new ArrayList<>(splinesPoints.size());
            for (List<UniformPoint3D> spline : splinesPoints) {
                parallel.add(spline.get(pIndex));
                for (int i = 1; i < INVISIBLE_SPLINES_K; i++) {
                    PointTranslator t = new PointTranslatorBuilder()
                            .shift(-center.getX(), -center.getY(), -center.getZ())
                            .rotateOverX(angleForInvisible * i)
                            .shift(center.getX(), center.getY(), center.getZ())
                            .build();
                    parallel.add(t.translate(spline.get(pIndex)));
                }
            }
            parallelsPoints.add(parallel);
        }
    }

    public void translate(PointTranslator t) {
        PointTranslator inner = new PointTranslatorBuilder()
                .shift(-center.getX(), -center.getY(), -center.getZ())
                .complex(t)
                .shift(center.getX(), center.getY(), center.getZ())
                .build();

        splinesPoints.replaceAll(
                lst -> lst.stream()
                        .map(inner::translate)
                        .collect(Collectors.toList())
        );

        parallelsPoints.replaceAll(
                lst -> lst.stream()
                        .map(inner::translate)
                        .collect(Collectors.toList())
        );
    }

    //region getters and setters
    public List<List<UniformPoint3D>> getSplinesPoints() {
        return splinesPoints;
    }

    public List<List<UniformPoint3D>> getParallelsPoints() {
        return parallelsPoints;
    }

    public static int getVisibleSplinesN() {
        return VISIBLE_SPLINES_N;
    }

    public static void setVisibleSplinesN(int visibleSplinesN) {
        Figure3D.VISIBLE_SPLINES_N = visibleSplinesN;
    }

    public static int getInvisibleSplinesCnt() {
        return INVISIBLE_SPLINES_K;
    }

    public static void setInvisibleSplinesCnt(int invisibleSplinesCnt) {
        Figure3D.INVISIBLE_SPLINES_K = invisibleSplinesCnt;
    }

    public static int getParallelsM() {
        return PARALLELS_M;
    }

    public static void setParallelsM(int parallelsM) {
        Figure3D.PARALLELS_M = parallelsM;
    }

    public UniformPoint3D getCenter() {
        return center;
    }

    public void setCenter(UniformPoint3D center) {
        this.center = center;
        initFigure();
    }
    //endregion

}

