/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Thread.State;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    private States state1 = States.END;
    private States state2 = States.END;
    private States state3 = States.END;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        if (task1 == null || state1 == States.END) {
            task1 = new Task1(2147483647, 1000000);
            task1.setNotificationTarget(this);
            task1.start();
        } else if (state1 == States.RUNNING) {
            task1.end();
            task1 = null;
        }
    }
    
    @Override
    public void notify(String message, States state) {
        if (message.equals("Task1 done.")) {
            task1 = null;
        }
        textArea.appendText(message + "\n");
        this.state1 = state;
        if (state1 == States.END) {
            button1.setText("Start Task1");
        } else if (state1 == States.RUNNING) {
            button1.setText("End Task1");
        }
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        if (task2 == null || state2 == States.END) {
            task2 = new Task2(2147483647, 1000000);
            task2.setOnNotification((String message, States state) -> {
                textArea.appendText(message + "\n");
                this.state2 = state;
                if (state2 == States.RUNNING) {
                    button2.setText("End Task2");
                } else {
                    button2.setText("Start Task2");
                }
                
            });
            
            task2.start();
        } else if (state2 == States.RUNNING) {
            task2.end();
            task2 = null;
        }     
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if (task3 == null || state3 == States.END) {
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if (evt.getPropertyName().equals("message")) {
                    textArea.appendText((String) evt.getNewValue() + "\n");
                } else if (evt.getPropertyName().equals("state")) {
                    state3 = (States) evt.getNewValue();
                }

                if (state3 == States.END) {
                    button3.setText("Start Task3");
                } else if (state3 == States.RUNNING) {
                    button3.setText("End Task3");
                }            
            });
            
            task3.start();
            
        } else if (state3 == States.RUNNING) {
            task3.end();
            task3 = null;
        }
    } 
}
