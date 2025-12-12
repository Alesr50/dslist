package br.com.ale.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ale.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/tasks")
@RestController
public class TaskController<HttpServerLetRequest> {
	
	@Autowired
	private ITaskRepository taskRepository;
	
	////retorna
	@PostMapping("/")
	public ResponseEntity create(@RequestBody TaskModel taskModel,HttpServletRequest request) {
		
		System.out.println("Chegou aqui...no controller");
		//Pegar o id do usuario logado
		var idUser = request.getAttribute("idUser");
			System.out.println("Id do usuario logado: " + idUser);
		taskModel.setIdUser((UUID)idUser);

		var currentDate = LocalDateTime.now();

		//08/12/2025
		//08/11/2025
			//Verificando se a data de incio é depois da de hoje e se a data de termino, não é 
		if (currentDate.isAfter(taskModel.getStartAt())||currentDate.isAfter(taskModel.getEndAt()) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/data de termino deve ser maior que a atual");


		}
		if (taskModel.getStartAt().isAfter(taskModel.getEndAt()) ){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio nao deve ser menor que a data de termino");


		}
		//var task = this.taskRepository.save(taskModel);
			var task = this.taskRepository.save(taskModel);

		return ResponseEntity.status(HttpStatus.OK).body(task);
		
	}

@GetMapping("/")
	public List<TaskModel> list(HttpServletRequest request){
		var idUser = request.getAttribute("idUser");
		var tasks = this.taskRepository.findByIdUser((UUID)idUser);
		return tasks;

	}
//TaskModel> objeto...ResponseEntity> Badrequest
	@PutMapping("/{id}")
	public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request ){
	    //var idUser = request.getAttribute("idUser");
	

		var task = this.taskRepository.findById(id).orElse(null);

		//Verificando se a tarefa existe
		if (task == null){
			//Usando ResponseEntity....mudar retorno do metodo
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
		}
		System.out.println(task.getId());
		//Usuario do request
		var idUser = request.getAttribute("idUser");
		System.out.println(idUser);
		if (!task.getIdUser().equals(idUser)){
			//Usando ResponseEntity....mudar retorno do metodo
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não tem permissão para alterar essa tarefa");
		}

		//Chamando o metedo de Utils para realizar a mescla
		Utils.copyNonNullProperties(taskModel,task);		
		//taskModel.setIdUser((UUID)idUser);
		//taskModel.setId(id);
		//return this.taskRepository.save(taskModel);
		//Validando usuario da taks para alteração, usando if e else		
		var taskUpdated = this.taskRepository.save(task);
		return  ResponseEntity.ok().body(taskUpdated);
    

	}
	
}
