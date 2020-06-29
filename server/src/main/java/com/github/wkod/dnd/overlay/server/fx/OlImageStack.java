package com.github.wkod.dnd.overlay.server.fx;

import java.util.Objects;

import com.github.wkod.dnd.overlay.server.config.Configuration;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

public class OlImageStack extends StackPane {

    private static int idcounter = 0;

    private final OlPane imagepane;
    private final ImageView imageview;
    private final Text lblname;

    @Getter
    private final String name;

    private double dragx;
    private double dragy;

    private int touch1PointId;
    private boolean touch1Active;
    private Point2D touch1Position;

    private int touch2PointId;
    private boolean touch2Active;
    private Point2D touch2Position;

    @Getter
    @Setter
    private boolean slotted = false;

    public OlImageStack(final OlPane imagepane, final String name, final Image image) {
        // set information
        setId(String.valueOf(idcounter++));
        this.imagepane = imagepane;
        this.name = name;

        if (!Configuration.IMAGE_TRANSPARENCY.get()) {
            setStyle("-fx-background-color: white;");
        }

        // add image
        imageview = new ImageView(image);
        imageview.setPreserveRatio(true);
        getChildren().add(imageview);

        // set sizes to 1/2 of screen (height)
        defaultScale();

        // add name label
        if (name != null) {
            lblname = new Text(name);
            lblname.setFont(new Font(Configuration.IMAGE_TEXT_SIZE.get()));
            lblname.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1px;");
            lblname.maxWidth(Double.MAX_VALUE);
            lblname.maxHeight(Double.MAX_VALUE);

            StackPane.setAlignment(lblname, Pos.valueOf(Configuration.IMAGE_TEXT_POSITION.get()));
            getChildren().add(lblname);
        } else {
            lblname = null;
        }

        // register Events
        setOnMousePressed(e -> {
            toFront();

            dragx = getLayoutX() - e.getSceneX();
            dragy = getLayoutY() - e.getSceneY();
        });

        setOnMouseDragged(e -> {
            setLayoutX(e.getSceneX() + dragx);
            setLayoutY(e.getSceneY() + dragy);
        });

        // touch events
        setOnTouchPressed(e -> {
            if (!touch1Active) {
                // first finger
                touch1Active = true;
                touch1PointId = e.getTouchPoint().getId();
                touch1Position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());

                toFront();
            } else if (!touch2Active) {
                // second finger
                touch2Active = true;
                touch2PointId = e.getTouchPoint().getId();
                touch2Position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());
            }

            e.consume();
        });

        setOnTouchMoved(e -> {
            if (touch1Active) {
                if (Objects.equals(touch1PointId, e.getTouchPoint().getId())) {
                    // move first finger
                    Point2D position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());

                    setLayoutX(getLayoutX() + position.getX() - touch1Position.getX());
                    setLayoutY(getLayoutY() + position.getY() - touch1Position.getY());

                    // update finger position
                    touch1Position = position;

                    if (slotted) {
                        this.imagepane.removeSlot(this);
                    }

                } else if (Objects.equals(touch2PointId, e.getTouchPoint().getId())) {
                    // move second finger
                    Point2D position = new Point2D(e.getTouchPoint().getSceneX(), e.getTouchPoint().getSceneY());

                    // update finger position
                    touch2Position = position;
                }
            }

            e.consume();
        });

        setOnTouchReleased(e -> {
            if (Objects.equals(touch1PointId, e.getTouchPoint().getId())) {
                touch1Active = false;

                // keep part of picture on screen
                if (getBoundsInParent().getMinX() + Configuration.IMAGE_SIZE_MIN_VISIBLE.get() > this.imagepane
                        .getWidth()) {
                    setLayoutX(this.imagepane.getWidth() - Configuration.IMAGE_SIZE_MIN_VISIBLE.get()
                            - (getBoundsInParent().getMinX() - getLayoutX()));
                } else if (getBoundsInParent().getMaxX() < Configuration.IMAGE_SIZE_MIN_VISIBLE.get()) {
                    setLayoutX(Configuration.IMAGE_SIZE_MIN_VISIBLE.get() - getBoundsInParent().getWidth()
                            - (getBoundsInParent().getMinX() - getLayoutX()));
                }

                if (getBoundsInParent().getMinY() + Configuration.IMAGE_SIZE_MIN_VISIBLE.get() > this.imagepane
                        .getHeight()) {
                    setLayoutY(this.imagepane.getHeight() - Configuration.IMAGE_SIZE_MIN_VISIBLE.get()
                            - (getBoundsInParent().getMinY() - getLayoutY()));
                } else if (getBoundsInParent().getMaxY() < Configuration.IMAGE_SIZE_MIN_VISIBLE.get()) {
                    setLayoutY(Configuration.IMAGE_SIZE_MIN_VISIBLE.get() - getBoundsInParent().getHeight()
                            - (getBoundsInParent().getMinY() - getLayoutY()));
                }
            } else if (Objects.equals(touch2PointId, e.getTouchPoint().getId())) {
                touch2Active = false;
            }

            e.consume();
        });

        setOnZoom(e -> {
            setScale(getScale() * e.getZoomFactor());
            e.consume();
        });

        setOnRotate(e -> {
            setRotate(getRotate() + e.getAngle());
            e.consume();
        });

        setOnSwipeRight(e -> {
            defaultScale();
            setRotate(0);
            this.imagepane.toSlot(this);
            e.consume();
        });

        setOnMouseClicked(e -> {
            if (MouseButton.PRIMARY.equals(e.getButton())) {
                setScale(getScale() * 1.1);
            } else {
                setScale(getScale() * 0.9);
            }
        });
    }

    private void setScale(final double scale) {
        imageview.setFitHeight(
                (imageview.getBoundsInParent().getMaxY() - imageview.getBoundsInParent().getMinY()) * scale);
    }

    private double getScale() {
        return imageview.getScaleY();
    }

    private void defaultScale() {
        double defaultHeight = (imagepane.getHeight() * Configuration.IMAGE_SCALE_ONLOAD.get());
        double relheightpercent = imageview.getImage().getHeight() / defaultHeight;
        double scalefactor = 1 / relheightpercent;

        setScale(scalefactor);
    }
}