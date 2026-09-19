package com.diego.task_manager_api;

import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.entity.Task;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.repository.TaskRepository;
import com.diego.task_manager_api.service.TaskService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskById_WhenTasksExists_ReturnsTaskResponse(){
        // ARRANGE
        Task task = new Task();
        task.setTitle("Pruebas Spring");
        task.setDescription("Probando Mockitos, JUnit");
        task.setCompleted(true);

        when(taskRepository.findById(7L))
                .thenReturn(Optional.of(task));

        // ACT
        TaskResponse taskResponse = taskService.getTaskById(7L);

        // ASSET
        // Comprobar que los dos valores son iguales
        assertEquals(task.getDescription(), taskResponse.getDescription());


    }
    @Test
    void getAllTasks_WhenTasksExist_ReturnsTaskResponseList(){

        // ARRANGE

        Task task1 = new Task();
        task1.setTitle("Probando la List<Task>");
        task1.setDescription("Prueba");
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setTitle("Segunda tarea");
        task2.setDescription("Prueba 2");
        task2.setCompleted(true);
        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));
        // ACT
        List<TaskResponse> taskResponses = taskService.getAllTasks();

        // ASSERT
        assertEquals(2,taskResponses.size());
        assertEquals(task1.getTitle(),taskResponses.getFirst().getTitle());
        assertEquals(task2.getTitle(),taskResponses.get(1).getTitle());

    }
    @Test
    void getTaskById_IfNotExists_ThrowsNotFoundException(){
        // ARRANGE
        when(taskRepository.findById(10L))
                .thenReturn(Optional.empty());

        // ACT
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(10L)
        );

    }
    @Test
    void createTask_WhenRequestIsValid_ReturnsTaskResponse() {
        // ARRANGE
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();

        createTaskRequest.setTitle("TaskRequest Válido");
        createTaskRequest.setDescription("Probando el TaskRequest válido");
        createTaskRequest.setCompleted(true);

        Task savedTask = new Task();
        savedTask.setTitle("SavedTask");
        savedTask.setDescription("Saved Task de prueba");
        savedTask.setCompleted(false);

        // ACT
        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskResponse taskResponse = taskService.createTask(createTaskRequest);
        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();

        // ASSERT
        assertEquals(savedTask.getTitle(), taskResponse.getTitle());
        assertEquals(savedTask.getDescription(), taskResponse.getDescription());
        assertEquals(savedTask.isCompleted(), taskResponse.isCompleted());
        assertEquals(
                createTaskRequest.getTitle(),
                capturedTask.getTitle()
        );
        assertEquals(createTaskRequest.getCompleted(), capturedTask.isCompleted());
        assertEquals(createTaskRequest.getDescription(), capturedTask.getDescription());
    }
    @Test
    void deleteTaskWhenTaskExists(){
        // ARRANGE
        Task taskDelete = new Task();
        taskDelete.setTitle("Task Delete");
        taskDelete.setDescription("Task para comprobar si se borran los tasks con el verify");
        taskDelete.setCompleted(false);

        when(taskRepository.findById(7L))
                .thenReturn(Optional.of(taskDelete));

        // ACT
        taskService.deleteTask(7L);
        verify(taskRepository).deleteById(7L);

    }
    @Test
    void deleteTaskWhenTaskNotExists(){
        // ARRANGE
        when(taskRepository.findById(7L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(7L)
        );
    }
}
