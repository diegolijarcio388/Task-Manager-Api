package com.diego.task_manager_api.service;
import com.diego.task_manager_api.dto.CreateTaskRequest;
import com.diego.task_manager_api.dto.TaskResponse;
import com.diego.task_manager_api.dto.UpdateTaskRequest;
import com.diego.task_manager_api.entity.Task;
import com.diego.task_manager_api.exception.TaskNotFoundException;
import com.diego.task_manager_api.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

/**
 * Servicio que contiene la lógica de negocio
 * relacionada con las tareas.
 */
@Service
public class TaskService {

    // Repositorio que accede a la base de datos.
    private final TaskRepository taskRepository;

    /**
     * Spring inyecta automáticamente el TaskRepository.
     */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Obtiene todas las tareas desde la base de datos.
     */
    public List <Task> getAllTasks(){
        return taskRepository.findAll();
    }

    /**
     * Crea un Task
     */

    public TaskResponse createTask (CreateTaskRequest TaskRequest){
        Task newTask = new Task();
        newTask.setTitle(TaskRequest.getTitle());
        newTask.setCompleted(TaskRequest.getCompleted());
        newTask.setDescription(TaskRequest.getDescription());
        Task savedTask = taskRepository.save(newTask);
        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.isCompleted(),
                savedTask.getCreatedAt()
        );

    }

    /**
     * Encontrar tarea por Id
     */
    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("La tarea con el id: " + id + " no existe."));
    }

    /**
     * Borrar tarea por id
     */

    public void deleteTask(Long id){
        Optional <Task> task = taskRepository.findById(id);
        if (task.isPresent()){
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException(
                    "No existe ninguna tarea con el id " + id
            );
        }
    }

    /**
     *  Actualizar tarea
     */
    public Task updateTask(Long id, UpdateTaskRequest taskRequest) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task existingTask = task.get();
            existingTask.setTitle(taskRequest.getTitle());
            existingTask.setDescription(taskRequest.getDescription());
            existingTask.setCompleted(taskRequest.getCompleted());
            return taskRepository.save(existingTask);
        } else {
            throw new TaskNotFoundException(
                    "No existe ninguna tarea con el id " + id
            );
        }
    }
}
