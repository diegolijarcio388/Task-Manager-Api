package com.diego.task_manager_api.service;
import com.diego.task_manager_api.dto.CreateTaskRequest;
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

    public Task createTask (CreateTaskRequest TaskRequest){
        Task newTask = new Task();
        newTask.setTitle(TaskRequest.getTitle());
        newTask.setCompleted(TaskRequest.getCompleted());
        newTask.setDescription(TaskRequest.getDescription());
        return taskRepository.save(newTask);
    }

    /**
     * Encontrar tarea por Id
     */
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
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
    public Task updateTask(Long id, Task taskActualizado) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task existingTask = task.get();
            existingTask.setTitle(taskActualizado.getTitle());
            existingTask.setDescription(taskActualizado.getDescription());
            existingTask.setCompleted(taskActualizado.isCompleted());
            return taskRepository.save(existingTask);
        } else {
            throw new TaskNotFoundException(
                    "No existe ninguna tarea con el id " + id
            );
        }
    }
}
