package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.Label;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.Optional;

public class Main extends Application {

    RubberBandSelection rubberBandSelection;
    File selectedFile = null;
    Image image = null;
    long MAX = 0;
    Pane Nu = new Pane();
    Boolean ifchange = false;
    double X =0;
    double Y = 0;
    int number = 1;
    boolean now = false;
    Image savenow;
    Image thisimage;
    Stage stage = new Stage();
    ImageView imageView = new ImageView();
    Scene scene2;
    double yaosuobi = 2.7;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        stage.setTitle("PIP");

        stage.setResizable(true);

        BorderPane root = new BorderPane();

        //root.setMaxSize(1920*0.6-50,1080*0.6);

        BorderPane root5 = new BorderPane();
        BorderPane root3 = new BorderPane();
        scene2 = new Scene(root5, 1920*0.6, 1080*0.6+100, Color.rgb(1,42,50));
        scene2.getStylesheets().add(getClass().getResource("main.css").toExternalForm());


        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());

        root5.setCenter(root3);

        root5.setTop(menuBar);

        Button viewbut = new Button("对比原图");
        viewbut.setId("viewbut");
        viewbut.setVisible(false);
        BorderPane root4 = new BorderPane();
        root4.setTop(viewbut);
        root4.setId("root4");
        BorderPane yasuoroot = new BorderPane();
        root4.setCenter(yasuoroot);

        Group imageplayer = new Group();
        root3.setRight(root4);
        root.setCenter(imageView);
        imageplayer.getChildren().add(imageView);
        root.setCenter(imageplayer);

        javafx.scene.control.Label sizelabel = new javafx.scene.control.Label("");
        Button yaosuobut = new Button("确定压缩");
        yaosuobut.setId("yasuobut");
        yaosuobut.setVisible(false);
        sizelabel.setVisible(false);
        yasuoroot.setBottom(sizelabel);
        root4.setBottom(yaosuobut);
        sizelabel.setStyle("-fx-font-size: 17px;-fx-text-fill: #fff");


        Button openbut = new Button("打开图片");
        openbut.setId("openbut");
        root3.setCenter(openbut);






        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("确定裁剪范围");
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println( "选择范围: " + selectionBounds);

                // crop the image
                Image newimage = crop( selectionBounds);
                imageView.setImage( newimage);
                savenow = imageView.getImage();
                root3.setLeft(Nu);
            }
        });
        contextMenu.getItems().add( cropMenuItem);

        // set context menu on image layer
        imageplayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageplayer, event.getScreenX(), event.getScreenY());
                }
            }
        });



        Menu fileMenu = new Menu("文件选项");
        MenuItem newMenuItem = new MenuItem("打开图片");
        //MenuItem newsMenuItem = new MenuItem("批量打开");
        MenuItem saveMenuItem = new MenuItem("保存图片");
        MenuItem exitMenuItem = new MenuItem("退出");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem,new SeparatorMenuItem(),saveMenuItem,
                new SeparatorMenuItem(), exitMenuItem);

        //Menu majekMenu = new Menu("魔法功能");
        MenuItem xaingaoMenuItem = new MenuItem("线稿提取");

        //majekMenu.getItems().addAll(xaingaoMenuItem);

        Menu setMenu = new Menu("图像处理");

        MenuItem insertMenuItem = new MenuItem("一键颜色反转");
        setMenu.getItems().add(insertMenuItem);

        MenuItem gammaMenuItem = new MenuItem("一键曝光补偿");
        setMenu.getItems().add(gammaMenuItem);

        MenuItem conMenuItem = new MenuItem("一键对比度拉伸");
        setMenu.getItems().add(conMenuItem);

        MenuItem black_wite_MenuItem = new MenuItem("一键主图获取");
        setMenu.getItems().add(black_wite_MenuItem);

        MenuItem huidu_MenuItem = new MenuItem("一键灰度图获取");
        setMenu.getItems().add(huidu_MenuItem);

        MenuItem ruienuItem = new MenuItem("一键锐化调整");
        setMenu.getItems().add(ruienuItem);

        MenuItem junMenuItem = new MenuItem("亮度调整");
        setMenu.getItems().add(junMenuItem);

        MenuItem baoheenuItem = new MenuItem("饱和度调整");
        MenuItem fangzhenItem = new MenuItem("仿真修复");

        setMenu.getItems().add(baoheenuItem);
        setMenu.getItems().add(fangzhenItem);

        Menu trMenu = new Menu("直方图输出");
        ToggleGroup tGroup = new ToggleGroup();
        MenuItem allItem = new MenuItem("全局直方图");


        MenuItem lightItem = new MenuItem("灰度直方图");


        trMenu.getItems().addAll(allItem, lightItem,
                new SeparatorMenuItem());

        Menu tutorialManeu = new Menu("红绿蓝直方图");
        MenuItem redItem1 = new MenuItem("红色直方图");
        MenuItem greenItem1 = new MenuItem("绿色直方图");
        MenuItem blueItem1 = new MenuItem("蓝色直方图");
        tutorialManeu.getItems().addAll(redItem1,greenItem1,blueItem1);

        trMenu.getItems().add(tutorialManeu);


        Menu centrlMenu = new Menu("图片编辑");
        MenuItem centrlMenu1 = new MenuItem("复原图像");
        MenuItem centrlMenuya = new MenuItem("图像压缩");
        MenuItem centrlMenu2 = new MenuItem("图像裁剪");
        MenuItem centrlMenu3 = new MenuItem("图像旋转");
        MenuItem centrlMenu4 = new MenuItem("水平翻转");
        MenuItem centrlMenu5 = new MenuItem("垂直翻转");



        centrlMenu.getItems().addAll(centrlMenu1, centrlMenuya,centrlMenu2,new SeparatorMenuItem(),centrlMenu3,centrlMenu4,centrlMenu5,
                new SeparatorMenuItem());



        menuBar.getMenus().addAll(fileMenu, setMenu, trMenu,centrlMenu);


        junMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Image saveImage = imageView.getImage();
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                System.out.println("亮度");


                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        Image li_image2=ImageLight(imageView.getImage(),new_val.doubleValue());
                        ImageView newimageview = new ImageView();
                        newimageview.setImage(li_image2);
                        imageView.setImage(li_image2);
                        savenow = imageView.getImage();
                        //root.setBottom(viewbut);
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });

            }
        });

        //对比一下
        viewbut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                if(!now){

                    Image image1 = thisimage;
                    imageView.setImage(image1);
                    root3.getLeft().setVisible(false);

                    root5.getTop().setVisible(false);
                    now = true;
                }else if(now){
                    Image image1 = savenow;
                    imageView.setImage(image1);
                    now = false;
                    root3.getLeft().setVisible(true);
                    root5.getTop().setVisible(true);
                }


            }
        });


        //仿真
        fangzhenItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Image saveImage = imageView.getImage();
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                System.out.println("仿真");


                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(50);
                gammaslider.setValue(0);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        int left = 0;
                        int right = 255;
                        left += new_val.doubleValue();
                        right -= new_val.doubleValue();
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        Image bao_image2=ImageBaohe(imageView.getImage(),new_val.doubleValue()+50);
                        imageView.setImage(bao_image2);
                        Image gamma_contrast=ImageContrast(imageView.getImage(),left,right);
                        imageView.setImage(gamma_contrast);
                        savenow = imageView.getImage();
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });

            }
        });







        baoheenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Image saveImage = imageView.getImage();
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                System.out.println("饱和度");


                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        Image bao_image2=ImageBaohe(imageView.getImage(),new_val.doubleValue());
                        imageView.setImage(bao_image2);
                        savenow = imageView.getImage();
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });

            }
        });


        //Add all the event handlers (this is a minimal GUI - you may try to do better)
        insertMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                System.out.println("底片");
                //At this point, "image" will be the original image
                //imageView is the graphical representation of an image
                //imageView.getImage() is the currently displayed image

                //Let's invert the currently displayed image by calling the invert function later in the code
                Image inverted_image=ImageInverter(imageView.getImage());
                //Update the GUI so the new image is displayed
                imageView.setImage(inverted_image);
                savenow = imageView.getImage();
                root3.setLeft(Nu);
            }
        });


        //线稿事件
        //Add all the event handlers (this is a minimal GUI - you may try to do better)
        xaingaoMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }

                //At this point, "image" will be the original image
                //imageView is the graphical representation of an image
                //imageView.getImage() is the currently displayed image

                //Let's invert the currently displayed image by calling the invert function later in the code
                Image _image = null;
                for (int i=0;i<2;i++) {
                    _image = ImageRui(imageView.getImage(), 5);
                    imageView.setImage(_image);
                }
                System.out.println("线稿");
                _image=ImageXiangao(_image);
                //Update the GUI so the new image is displayed
                imageView.setImage(_image);
                savenow = imageView.getImage();
                root3.setLeft(Nu);
            }
        });


        gammaMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                Image saveImage = imageView.getImage();
                System.out.println("伽马矫正");
                double gamma=2.2;
                Image gamma_image=ImageGamma(imageView.getImage(),2.2);
                imageView.setImage(gamma_image);
                savenow = imageView.getImage();
                //拉动条
                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        Image gamma_image2=ImageGamma(imageView.getImage(),1.1*(new_val.doubleValue()/25d));
                        imageView.setImage(gamma_image2);
                        savenow = imageView.getImage();
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });
            }


        });


        conMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                Image saveImage = imageView.getImage();
                System.out.println("对比度拉伸");
                int a[] = AppropriateScope(imageView.getImage(),0.05);
                Image gamma_contrast=ImageContrast(imageView.getImage(),a[0],a[1]);
                imageView.setImage(gamma_contrast);
                savenow = imageView.getImage();
                Image imageold = new Image("file:"+selectedFile.getPath());


                //拉动条
                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {



                        int left;
                        int right;
//                      int b[] = AppropriateScope(imageold,new_val.doubleValue()*0.001);
                        if(new_val.doubleValue()>=0&&new_val.doubleValue()<=50){
                            left = (int)(0+((new_val.doubleValue()/50)*a[0]));
                            right = (int)(255-(((new_val.doubleValue())/50)*(255-a[1])));
                        }else{
                            left = (int)(a[0]+(((new_val.doubleValue()-50)/50)*(MAX-a[0])));
                            right = (int)(a[1]-(((new_val.doubleValue()-50)/50)*(a[1]-MAX)));
                        }
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        Image gamma_contrast=ImageContrast(imageold,left,right);
                        imageView.setImage(gamma_contrast);
                        savenow = imageView.getImage();
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });
            }
        });


        //主（黑白）图获取
        black_wite_MenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                Image saveImage = imageView.getImage();
                System.out.println("主图信息获取");
                Image gamma_contrast=ImageGetMainMes(imageView.getImage(),0.333F);
                imageView.setImage(gamma_contrast);
                savenow = imageView.getImage();



                //拉动条
                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(30);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                gammaslider.setOrientation(Orientation.VERTICAL);
                root3.setLeft(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        Image image1 = saveImage;
                        imageView.setImage(image1);
                        System.out.println(new_val);
                        Image gamma_contrast=ImageGetMainMes(imageView.getImage(),new_val.floatValue()/100f);

                        imageView.setImage(gamma_contrast);
                        savenow = imageView.getImage();
                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });
            }
        });




        //灰度图获取
        huidu_MenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }
                Image saveImage = imageView.getImage();
                System.out.println("灰度信息获取");
                Image gamma_contrast=ImageGetHuiMes(imageView.getImage());
                imageView.setImage(gamma_contrast);
                savenow = imageView.getImage();
                root3.setLeft(Nu);
            }
        });




        allItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }

                System.out.println("直方图");

                Imagehistogram(imageView.getImage());
            }
        });


        centrlMenu4.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("水平翻转");

                imageView.setImage(Xchange(imageView.getImage()));
                savenow = imageView.getImage();
            }
        });


        centrlMenu3.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("旋转");


                imageView.setImage(terchange(imageView.getImage()));
                number++;
                viewbut.setVisible(false);
                if(number%2==0){
                    imageView.setScaleX((Y/X)*imageView.getScaleX());
                    imageView.setScaleY((X/Y)*imageView.getScaleY());
                    savenow = imageView.getImage();

                }else {
                    imageView.setScaleX(imageView.getScaleX()*(Y/X));
                    imageView.setScaleY(imageView.getScaleY()*(X/Y));
                    savenow = imageView.getImage();

                }

                double temp = X;
                X = Y;
                Y = temp;
            }
        });



        //裁剪
        centrlMenu2.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("裁剪");

                //if(event == MouseEvent)


                Image mouse = new Image("mouse.png");
                ImageCursor Mouse=new ImageCursor(mouse,7.5,7.5) ; //20，20表示光标图片的偏移量
                //在scene中修改光标
                scene2.setCursor(Mouse);
                rubberBandSelection = new RubberBandSelection(imageplayer);
                //savenow = imageView.getImage();
            }
        });



        //压缩but
        yaosuobut.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Image newimage1 = crop(new Bounds(
                        imageView.getBoundsInParent().getMinX()+1,
                        imageView.getBoundsInParent().getMinY()+1,
                        0,
                        imageView.getBoundsInParent().getWidth(),
                        imageView.getBoundsInParent().getHeight(),
                        imageView.getBoundsInParent().getDepth()){
                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(Point2D p) {
                        return false;
                    }

                    @Override
                    public boolean contains(Point3D p) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double z) {
                        return false;
                    }

                    @Override
                    public boolean contains(Bounds b) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double w, double h) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double z, double w, double h, double d) {
                        return false;
                    }

                    @Override
                    public boolean intersects(Bounds b) {
                        return false;
                    }

                    @Override
                    public boolean intersects(double x, double y, double w, double h) {
                        return false;
                    }

                    @Override
                    public boolean intersects(double x, double y, double z, double w, double h, double d) {
                        return false;
                    }
                });

                imageView.setImage(newimage1);
                savenow = imageView.getImage();
                saveimage(newimage1);
                yaosuobut.setVisible(false);
                sizelabel.setVisible(false);
                root3.setLeft(Nu);
            }
        });
        //压缩menuitem
        centrlMenuya.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                sizelabel.setVisible(true);
                yaosuobut.setVisible(true);
                Image newimage = crop(new Bounds(
                        imageView.getBoundsInParent().getMinX()+1,
                        imageView.getBoundsInParent().getMinY()+1,
                        0,
                        imageView.getBoundsInParent().getWidth(),
                        imageView.getBoundsInParent().getHeight(),
                        imageView.getBoundsInParent().getDepth()){
                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(Point2D p) {
                        return false;
                    }

                    @Override
                    public boolean contains(Point3D p) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double z) {
                        return false;
                    }

                    @Override
                    public boolean contains(Bounds b) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double w, double h) {
                        return false;
                    }

                    @Override
                    public boolean contains(double x, double y, double z, double w, double h, double d) {
                        return false;
                    }

                    @Override
                    public boolean intersects(Bounds b) {
                        return false;
                    }

                    @Override
                    public boolean intersects(double x, double y, double w, double h) {
                        return false;
                    }

                    @Override
                    public boolean intersects(double x, double y, double z, double w, double h, double d) {
                        return false;
                    }
                });
                root3.setLeft(Nu);
            }
        });


        centrlMenu5.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("水平翻转");


                imageView.setImage(Ychange(imageView.getImage()));
                savenow = imageView.getImage();
            }
        });


        redItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("红色直方图");
                Imagehistogramred(imageView.getImage());
            }
        });

        greenItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("绿色直方图");
                ImagehistogramGreen(imageView.getImage());
            }
        });

        blueItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("蓝色直方图");
                ImagehistogramBlue(imageView.getImage());
            }
        });

        lightItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("亮度直方图");
                ImagehistogramLight(imageView.getImage());
            }
        });


        ruienuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange = true;
                if(ifchange){
                    viewbut.setVisible(true);
                }

                Image rui_contrast=ImageRui(imageView.getImage(),5);
                imageView.setImage(rui_contrast);
                savenow = imageView.getImage();
                root3.setLeft(Nu);
                System.out.println("锐化");
            }
        });

        openbut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root3.setCenter(root);
                System.out.println("打开图片");
                yaosuobut.setVisible(false);
                sizelabel.setVisible(false);
                image = openimage();
                thisimage = image;
                savenow = image;

                now = false;
                if(image == null){
                    root3.setCenter(openbut);
                    return;
                }
                X = image.getWidth();
                Y = image.getHeight();
                if(image.getHeight()/image.getWidth()>0.5){
                    imageView.setFitHeight(((1080*0.6-45)/image.getHeight())*image.getHeight());
                    imageView.setFitWidth(((1080*0.6-45)/image.getHeight())*image.getWidth());

                }else {
                    imageView.setFitHeight(((1920*0.55-45)/image.getWidth())*image.getHeight());
                    imageView.setFitWidth(((1920*0.55-45)/image.getWidth())*image.getWidth());
                }
                imageView.setScaleX(1);
                imageView.setScaleY(1);
                number = 1;
                imageView.setImage(image);
                sizelabel.setText((int)(imageView.getBoundsInParent().getWidth()*imageView.getBoundsInParent().getHeight()*yaosuobi)/1024+" KB");


                //拉动条
                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                root3.setTop(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        sizelabel.setText((int)(imageView.getBoundsInParent().getWidth()*imageView.getBoundsInParent().getHeight()*yaosuobi)/1024+" KB");
                        if(number%2 !=0){
                            System.out.println(sizelabel.getText());
                            imageView.setScaleX(new_val.doubleValue()/50+(1-new_val.doubleValue()/50)*0.2);
                            imageView.setScaleY(new_val.doubleValue()/50+(1-new_val.doubleValue()/50)*0.2);
                        }else {

                            System.out.println(sizelabel.getText());
                            double X1 = imageView.getImage().getWidth();
                            double Y1 = imageView.getImage().getHeight();
                            imageView.setScaleX((new_val.doubleValue()/50)*(X1/Y1)+(1-new_val.doubleValue()/50)*0.2*(X1/Y1));
                            imageView.setScaleY((new_val.doubleValue()/50)*(Y1/X1)+(1-new_val.doubleValue()/50)*0.2*(Y1/X1));
                        }

                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });
                root3.setLeft(Nu);
                viewbut.setVisible(false);

            }
        });

        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                root3.setCenter(root);
                System.out.println("打开图片");
                yaosuobut.setVisible(false);
                sizelabel.setVisible(false);
                image = openimage();
                thisimage = image;
                savenow = image;
                now = false;
                if(image == null){
                    root3.setCenter(openbut);
                    return;
                }
                X = image.getWidth();
                Y = image.getHeight();
                if(image.getHeight()/image.getWidth()>0.5){
                    imageView.setFitHeight(((1080*0.6-45)/image.getHeight())*image.getHeight());
                    imageView.setFitWidth(((1080*0.6-45)/image.getHeight())*image.getWidth());

                }else {
                    imageView.setFitHeight(((1920*0.55-45)/image.getWidth())*image.getHeight());
                    imageView.setFitWidth(((1920*0.55-45)/image.getWidth())*image.getWidth());
                }

                imageView.setScaleX(1);
                imageView.setScaleY(1);
                number = 1;
                imageView.setImage(image);
                sizelabel.setText((int)(imageView.getBoundsInParent().getWidth()*imageView.getBoundsInParent().getHeight()*yaosuobi)/1024+" KB");


                //拉动条
                Slider gammaslider = null;
                gammaslider = new Slider();
                gammaslider.setMin(0);
                gammaslider.setMax(100);
                gammaslider.setValue(50);
                gammaslider.setShowTickLabels(true);
                gammaslider.setShowTickMarks(true);
                gammaslider.setMajorTickUnit(50);
                gammaslider.setMinorTickCount(5);
                gammaslider.setBlockIncrement(10);
                root3.setTop(gammaslider);
                final Label opacityValue = new Label(Double.toString(gammaslider.getValue()));
                gammaslider.valueProperty().addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number> ov,
                                        Number old_val, Number new_val) {
                        sizelabel.setText((int)(imageView.getBoundsInParent().getWidth()*imageView.getBoundsInParent().getHeight()*yaosuobi)/1024+" KB");
                        if(number%2 !=0){
                            imageView.setScaleX(new_val.doubleValue()/50+(1-new_val.doubleValue()/50)*0.2);
                            imageView.setScaleY(new_val.doubleValue()/50+(1-new_val.doubleValue()/50)*0.2);
                        }else {
                            double X1 = imageView.getImage().getWidth();
                            double Y1 = imageView.getImage().getHeight();
                            imageView.setScaleX((new_val.doubleValue()/50)*(X1/Y1)+(1-new_val.doubleValue()/50)*0.2*(X1/Y1));
                            imageView.setScaleY((new_val.doubleValue()/50)*(Y1/X1)+(1-new_val.doubleValue()/50)*0.2*(Y1/X1));
                        }


                        opacityValue.setText(String.format("%.2f", new_val));
                    }
                });
                root3.setLeft(Nu);
                viewbut.setVisible(false);

            }
        });

        centrlMenu1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                ifchange =false;
                System.out.println("复原图片");
                Image image1 = new Image("file:"+selectedFile.getPath());
                imageView.setImage(image1);
                root3.setLeft(Nu);
                viewbut.setVisible(false);
                savenow = imageView.getImage();
            }
        });

        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile == null ){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("您还未打开任何图片！");
                    alert.setContentText("单击确认继续");
                    alert.showAndWait();
                    return;
                }
                System.out.println("保存图片");
                Image image2 = imageView.getImage();
                saveimage(image2);
            }
        });

        stage.setScene(scene2);
        stage.show();
    }

    //裁剪
    public Image crop( Bounds bounds) {
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        //parameters.setFill(Color.rgb(0,0,0));
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);


        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);


        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        Image imagenew = SwingFXUtils.toFXImage(bufImageRGB, null);


        graphics.dispose();
        return imagenew;


    }


    public  class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group = new Group();

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;


            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.rgb(60,60,100));
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.1));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);


        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);


                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }

            }

        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                scene2.setCursor(null);
                if( event.isSecondaryButtonDown())
                    return;

            }
        };
        private  final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;

        }
    }


    //水平翻转
    public Image Xchange(Image image) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(width, height);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x <width; x++) {
                Color color = image_reader.getColor(x, y);
                inverted_image_writer.setColor(width-x-1, y, color);
            }
        }
        return inverted_image;
    }

    //垂直翻转
    public Image Ychange(Image image) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(width, height);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x <width; x++) {
                Color color = image_reader.getColor(x, y);
                inverted_image_writer.setColor(x, height-y-1, color);
            }
        }
        return inverted_image;
    }


    //剪切代码
    public Image Cut(Image image,int x1,int y1,int x2,int y2) {

//        int width = (int)image.getWidth();
//        int height = (int)image.getHeight();
//        WritableImage inverted_image = new WritableImage(width, height);
//        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
//        PixelReader image_reader=image.getPixelReader();
        ImageView cutimageview = new ImageView();
        cutimageview.setImage(image);
        //ViewPort cutport = new ViewPort(x1,y1,x2,y2);
        Rectangle clip = new Rectangle(100,100);
        cutimageview.setClip(clip);


        return cutimageview.getImage();
    }


    //图像旋转
    public Image terchange(Image image) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(height, width);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x <width; x++) {
                Color color = image_reader.getColor(x, y);
                inverted_image_writer.setColor(height-y-1,x, color);
            }
        }
        Image imagex = inverted_image;

        return imagex;
    }

    //饱和度
    public Image ImageBaohe(Image image,double value) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(width, height);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                double rgbmax = color.getRed();
                double rgbmin = color.getGreen();
                double rgbc = color.getBlue();
                if(rgbmax<rgbc){
                    double temp = rgbmax;
                    rgbmax = rgbc;
                    rgbc = temp;
                }
                if(rgbmax<rgbmin){
                    double temp = rgbmax;
                    rgbmax = rgbmin;
                    rgbmin = temp;
                }
                if(rgbmin>rgbc){
                    double temp = rgbmin;
                    rgbmin = rgbc;
                    rgbc = temp;
                }

                double delta = rgbmax - rgbmin;
                if(delta == 0) {
                    inverted_image_writer.setColor(x, y, color);
                    continue;
                }

                double evalue = rgbmax + rgbmin;
                double S, L = evalue / 2;
                if (L < 0.5)
                    S = delta / evalue;
                else
                    S = delta  / (2 - evalue);
                // 具体的饱和度调整，sValue为饱和度增减量
                // 如果增减量>0，饱和度呈级数增强，否则线性衰减
                double cvalue = ((value-50)/65);
                if (cvalue > 0)
                {
                    // 如果增减量+S > 1，用S代替增减量，以控制饱和度的上限
                    // 否则取增减量的补数
                    cvalue = cvalue + S >= 1? S : 1 - cvalue;
                    // 求倒数 - 1，实现级数增强
                    cvalue = 1 / cvalue - 1;
                }
                // L在此作饱和度下限控制
                double red = color.getRed() + (color.getRed() - L) * cvalue;
                double green = color.getGreen() + (color.getGreen() - L) * cvalue;
                double blue = color.getBlue() + (color.getBlue() - L) * cvalue;

                if(red < 0) red =0;
                if(red > 1) red = 1;
                if(green < 0) green =0;
                if(green > 1) green = 1;
                if(blue < 0) blue =0;
                if(blue > 1) blue = 1;

                color=Color.color(red, green, blue);
                inverted_image_writer.setColor(x, y, color);
            }
        }
        return inverted_image;
    }


    //亮度增算法
    public Image ImageLight(Image image,double value) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(width, height);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                red += (value-50)/65;
                blue += (value-50)/65;
                green += (value-50)/65;

                if(red < 0) red =0;
                if(red > 1) red = 1;
                if(green < 0) green =0;
                if(green > 1) green = 1;
                if(blue < 0) blue =0;
                if(blue > 1) blue = 1;

                color=Color.color(red, green, blue);
                inverted_image_writer.setColor(x, y, color);
            }
        }
        return inverted_image;
    }

    //打开图片
    public Image openimage(){


        Stage slStage = null;
        FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例

        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JPG", "*.jpg"),
                new ExtensionFilter("GIF", "*.gif"),
                new ExtensionFilter("BMP", "*.bmp"),
                new ExtensionFilter("PNG", "*.png")
        );
        selectedFile = fileChooser.showOpenDialog(slStage);
        try{
            String str = selectedFile.getPath();

            Image image1 = new Image("file:"+str);
            return image1;
        }catch (Exception e){
            return null;
        }

    }

    //保存图片
    public void saveimage(Image image){
        Stage slStage = null;



        TextInputDialog dialog = new TextInputDialog(selectedFile.getName());
        dialog.setTitle("保存图片");


        DirectoryChooser directoryChooser=new DirectoryChooser();
        File file = directoryChooser.showDialog(slStage);
        String path = file.getPath();


        dialog.setHeaderText("");
        dialog.setContentText("请输入你的文件名：");
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println("Your name: " + result.get());
            System.out.println(path);
            File outputFile = new File(path+"\\"+result.get());
            if(image == null) return;
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(bImage, "png", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> System.out.println("Your name: " + name));


    }

    //Example function of invert
    public Image ImageInverter(Image image) {
        //获得宽高
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        //画布
        WritableImage inverted_image = new WritableImage(width, height);
        //画笔工具
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        //吸管工具
        PixelReader image_reader=image.getPixelReader();

        //图像处理
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                //获得每个像素点
                Color color = image_reader.getColor(x, y);
                //处理已归一化处理的像素
                color=Color.color(1.0-color.getRed(), 1.0-color.getGreen(), 1.0-color.getBlue());
                //绘制
                inverted_image_writer.setColor(x, y, color);
            }
        }
        return inverted_image;
    }

    //线稿提取
    public Image ImageXiangao(Image image) {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage inverted_image = new WritableImage(width, height);
        PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);

                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();
                if(red > 0.01||green > 0.01||blue > 0.01){
                    Color newcolor = Color.color(1,1,1);
                    inverted_image_writer.setColor(x, y, newcolor);
                    continue;
                }
                Color newcolor = Color.color(0,0,0);
                inverted_image_writer.setColor(x, y, newcolor);

            }
        }
        return inverted_image;
    }

    //Gamma矫正
    public Image ImageGamma(Image image,double gamma) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage gamma_image = new WritableImage(width, height);

        PixelWriter gamma_image_writer = gamma_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        double save[] = new double[256];
        for(int i=0;i<save.length;i++){
            save[i] = (Math.pow((((double)i+0.5)/256.0),1.0/gamma))*256.0-0.5;
            if(save[i] < 0) save[i] = 0;
            if(save[i] >255) save[i] = 255;
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {

                Color color = image_reader.getColor(x, y);
                color=Color.color(save[(int)(color.getRed()*255)]/255, save[(int)(color.getGreen()*255)]/255, save[(int)(color.getBlue()*255)]/255);
                gamma_image_writer.setColor(x, y, color);
            }
        }
        return gamma_image;
    }


    //找到合适的拉伸范围
    public int [] AppropriateScope(Image image,double maximumLoss){
        int scope[] = new int[2];
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getRed()*255*0.3+color.getGreen()*255*0.59+color.getBlue()*255*0.11)]++;
            }
        }

        //确定图片最大集中灰度
        long max = histogram[0];
        for(int i = 0;i<histogram.length;i++){
            if(max < histogram[i]){
                MAX = i;
                max = histogram[i];
            }
        }


        long pxmax = 0;
        for (int i=0;i<histogram.length;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }
        long sum1 = 0;
        for(int i=0;i<histogram.length;i++){
            sum1 += histogram[i];
            if(sum1 >= width*height*3*maximumLoss){
                scope[0] = i;
                break;
            }
        }
        long sum2 = 0;
        for(int i=255;i>0;i--){
            sum2 += histogram[i];
            if(sum2 >= width*height*3*maximumLoss){
                scope[1] = i;
                break;
            }
        }
        return scope;
    }


    //锐化算法
    public Image ImageRui(Image image,double value) {

        value = 4+2*(value/10);
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        float[] data = { -1.0f, -1.0f, -1.0f, -1.0f, 10.0f, -1.0f, -1.0f, -1.0f, -1.0f };

        for(int y = 1; y < height-1; y++) {
            for(int x = 1; x < width-1; x++) {
                int sumpx = 0;
                Color color = image_reader.getColor(x, y);
                Color color1 = image_reader.getColor(x-1, y);
                Color color2 = image_reader.getColor(x+1, y);
                Color color3 = image_reader.getColor(x, y-1);
                Color color4 = image_reader.getColor(x, y+1);
                double redrui = value*color.getRed()-(color1.getRed()+color2.getRed()+color3.getRed()+color4.getRed());
                if(redrui<0) redrui = 0;
                if(redrui >1) redrui =1;
                double greenrui = value*color.getGreen()-(color1.getGreen()+color2.getGreen()+color3.getGreen()+color4.getGreen());
                if(greenrui<0) greenrui = 0;
                if(greenrui >1) greenrui =1;
                double bluerui = value*color.getBlue()-(color1.getBlue()+color2.getBlue()+color3.getBlue()+color4.getBlue());
                if(bluerui<0) bluerui = 0;
                if(bluerui >1) bluerui =1;
                color=Color.color(redrui, greenrui, bluerui);
                contrast_image_writer.setColor(x, y, color);
            }
        }
        return contrast_image;
    }


    //对比度拉伸
    public Image ImageContrast(Image image,int min,int max) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        double contrastd[] = new double[256];

        for(double i:contrastd){
            i = ((i-(double)min)/((double)max-(double) min))*255;
            if(i>255) i = 255.0;
            if(i<0) i=0.0;
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                color=Color.color(contrastd[(int)(color.getRed()*255)]/255d, contrastd[(int)(color.getGreen()*255)]/255d, contrastd[(int)(color.getBlue()*255)]/255d);
                contrast_image_writer.setColor(x, y, color);
            }
        }
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                color=Color.color(contrastd[(int)(color.getRed()*255)]/255d, contrastd[(int)(color.getGreen()*255)]/255d, contrastd[(int)(color.getBlue()*255)]/255d);
                contrast_image_writer.setColor(x, y, color);
            }
        }

//        for(int i=0;i<min;i++){
//            contrastd[i]=0.0;
//        }
//        for(int i=max;i<=255;i++){
//            contrastd[i]=1.0;
//        }
//
//        double fazhi = 5.53733d;
//        double stip = (fazhi*2)/((double) (max-min));
//
//        for(int i=min;i<=max;i++){
//            double x=((double) (i-min))*stip-fazhi;
//            contrastd[i] = 1d/(1+Math.pow(2.71828182846d,-x));
//            if(contrastd[i]>1d) contrastd[i] = 1d;
//            else if(contrastd[i]<0d) contrastd[i]=0.0d;
//        }
//
//        for(int y = 0; y < height; y++) {
//            for(int x = 0; x < width; x++) {
//                Color color = image_reader.getColor(x, y);
//                color=Color.color(contrastd[(int)(color.getRed()*255)], contrastd[(int)(color.getGreen()*255)], contrastd[(int)(color.getBlue()*255)]);
//                contrast_image_writer.setColor(x, y, color);
//            }
//        }

        return contrast_image;
    }

    //主图获取算法
    public Image ImageGetMainMes(Image image,float bilv) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();
        double findPx =0;
        double allpx =0;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                allpx += (color.getBlue() + color.getRed()+color.getGreen());
            }
        }
        findPx = allpx/(width*height*3)*bilv;



        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                if((color.getBlue() + color.getRed()+color.getGreen())/3<=findPx){
                    color = Color.color(0,0,0);
                }else{
                    color = Color.color(1,1,1);
                }
                contrast_image_writer.setColor(x, y, color);
            }
        }
        return contrast_image;
    }

    //灰度图获取算法
    public Image ImageGetHuiMes(Image image) {

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Color color = image_reader.getColor(x, y);
                double colorValue = (color.getBlue()+color.getRed()+color.getGreen())/3d;
                Color color2 = Color.color((float)colorValue,(float)colorValue,(float)colorValue);
                contrast_image_writer.setColor(x, y, color2);
            }
        }
        return contrast_image;
    }

    //全局直方图
    public Image Imagehistogram(Image image) {
        if(selectedFile == null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("您还未打开任何图片！");
            alert.setContentText("单击确认继续");
            alert.showAndWait();
            return null;
        }

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getRed()*255)]++;
                histogram[(int)(color.getGreen()*255)]++;
                histogram[(int)(color.getBlue()*255)]++;
            }
        }
        long pxmax = 0;
        for (int i=1;i<histogram.length-1;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }


        double ratio = 500d/pxmax;

        Stage primaryStage = new Stage();
        primaryStage.setTitle("图像直方图");
        Group root2 = new Group();
        Scene scene2 = new Scene(root2, 255*2+10, 500, Color.rgb(255,255,255));
        for (int i=0;i<histogram.length;i++){
            Rectangle r = new Rectangle();
            r.setX(i*2+5);
            r.setY(500-histogram[i]*ratio-3);
            r.setWidth(2);
            r.setHeight(histogram[i]*ratio);

            root2.getChildren().add(r);
        }


        primaryStage.setScene(scene2);
        primaryStage.show();

        return contrast_image;
    }

    //红色通道直方图
    public Image Imagehistogramred(Image image) {
        if(selectedFile == null ){
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error!");
            alert1.setHeaderText("您还未打开任何图片！");
            alert1.setContentText("单击确认继续");
            alert1.showAndWait();
            return null;
        }

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getRed()*255)]++;
            }
        }
        long pxmax = 0;
        for (int i=1;i<histogram.length-1;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }

        double ratio = 500d/pxmax;

        Stage primaryStage = new Stage();
        primaryStage.setTitle("红色方图");
        Group root2 = new Group();
        Scene scene2 = new Scene(root2, 255*2+10, 500, Color.rgb(255,255,255));
        for (int i=0;i<histogram.length;i++){
            Rectangle r = new Rectangle();
            r.setFill(Color.RED);
            r.setX(i*2+5);
            r.setY(500-histogram[i]*ratio-3);
            r.setWidth(2);
            r.setHeight(histogram[i]*ratio);

            root2.getChildren().add(r);
        }


        primaryStage.setScene(scene2);
        primaryStage.show();

        return contrast_image;
    }

    //绿色通道直方图
    public Image ImagehistogramGreen(Image image) {
        if(selectedFile == null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("您还未打开任何图片！");
            alert.setContentText("单击确认继续");
            alert.showAndWait();
            return null;
        }

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getGreen()*255)]++;
            }
        }
        long pxmax = 0;
        for (int i=1;i<histogram.length-1;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }

        double ratio = 500d/pxmax;

        Stage primaryStage = new Stage();
        primaryStage.setTitle("绿色方图");
        Group root2 = new Group();
        Scene scene2 = new Scene(root2, 255*2+10, 500, Color.rgb(255,255,255));
        for (int i=0;i<histogram.length;i++){
            Rectangle r = new Rectangle();
            r.setFill(Color.GREEN);
            r.setX(i*2+5);
            r.setY(500-histogram[i]*ratio-3);
            r.setWidth(2);
            r.setHeight(histogram[i]*ratio);

            root2.getChildren().add(r);
        }


        primaryStage.setScene(scene2);
        primaryStage.show();

        return contrast_image;
    }

    //蓝色通道直方图
    public Image ImagehistogramBlue(Image image) {
        if(selectedFile == null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("您还未打开任何图片！");
            alert.setContentText("单击确认继续");
            alert.showAndWait();
            return null;
        }

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getBlue()*255)]++;
            }
        }
        long pxmax = 0;
        for (int i=1;i<histogram.length-1;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }

        double ratio = 500d/pxmax;

        Stage primaryStage = new Stage();
        primaryStage.setTitle("蓝色方图");
        Group root2 = new Group();
        Scene scene2 = new Scene(root2, 255*2+10, 500, Color.rgb(255,255,255));
        for (int i=0;i<histogram.length;i++){
            Rectangle r = new Rectangle();
            r.setFill(Color.BLUE);
            r.setX(i*2+5);

            r.setY(500-histogram[i]*ratio-3);
            r.setWidth(2);
            r.setHeight(histogram[i]*ratio);

            root2.getChildren().add(r);
        }


        primaryStage.setScene(scene2);
        primaryStage.show();

        return contrast_image;
    }

    //亮度直方图
    public Image ImagehistogramLight(Image image) {
        if(selectedFile == null ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("您还未打开任何图片！");
            alert.setContentText("单击确认继续");
            alert.showAndWait();
            return null;
        }

        int width = (int)image.getWidth();
        int height = (int)image.getHeight();

        WritableImage contrast_image = new WritableImage(width, height);

        PixelWriter contrast_image_writer = contrast_image.getPixelWriter();

        PixelReader image_reader=image.getPixelReader();

        long histogram[] = new long[256];
        for (int k=0;k<histogram.length;k++){
            histogram[k] = 0;
        }
        for(int i=1;i<width;i++){
            for (int j=1;j<height;j++){
                Color color = image_reader.getColor(i, j);
                histogram[(int)(color.getRed()*255*0.3+color.getGreen()*255*0.59+color.getBlue()*255*0.11)]++;
            }
        }
        long pxmax = 0;
        for (int i=1;i<histogram.length-1;i++){
            if(pxmax<histogram[i]) pxmax=histogram[i];
        }
        double ratio = 500d/pxmax;

        Stage primaryStage = new Stage();
        primaryStage.setTitle("亮度方图");
        Group root2 = new Group();
        Scene scene2 = new Scene(root2, 255*2+10, 500, Color.rgb(255,255,255));
        for (int i=0;i<histogram.length;i++){
            Rectangle r = new Rectangle();
            r.setX(i*2+5);
            r.setY(500-histogram[i]*ratio-3);
            r.setWidth(2);
            r.setHeight(histogram[i]*ratio);

            root2.getChildren().add(r);
        }



        primaryStage.setScene(scene2);
        primaryStage.show();

        return contrast_image;
    }

    public static void main(String[] args) {
        launch();
    }

}
