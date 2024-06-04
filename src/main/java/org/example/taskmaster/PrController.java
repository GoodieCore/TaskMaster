package org.example.taskmaster;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import project.Project;
/**
 * Контроллер для управления отображением проектов.
 * Отображает информацию о каждом проекте и инициализирует их данные.
 * Реализует инициализацию интерфейса Initializable.
 *
 * @author Дмитрий Куков
 * @date 28.05.2024
 */
public class PrController  {
    @FXML
    private Label name;
    @FXML
    private Label countUs;
    @FXML
    private Label term;
    @FXML
    private Label status;

    public void setData(Project project) {
        try {
            // Обработка имени
            if (project.getName() != null) {
                name.setText(project.getName());
            } else {
                name.setText("-");
            }

            // Обработка количества пользователей
            countUs.setText(project.getCountUs() + " человек");

            // Обработка срока
            if (project.getTerm() != null) {
                term.setText(project.getTerm());
            } else {
                term.setText("-");
            }

            // Обработка статуса
            if (project.getStatus() != null) {
                status.setText(project.getStatus());
            } else {
                status.setText("-");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Произошла ошибка при установке значений для проекта");
        }
    }
}