package br.com.ale.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ale.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    IUserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

             var servletPath = request.getServletPath();
             //Alterar pra receber o ID
             //if(servletPath.equals("/tasks/") )
             if(servletPath.startsWith("/tasks/") ){


                  // Pegar a autenticação do usuário 
        var autorization = request.getHeader("Authorization");

        //Decodificar, pegar somente o token
        var authEncoded= autorization.substring("Basic".length()).trim();
        //Array de bytes do token
        byte[] authDecode = Base64.getDecoder().decode(authEncoded);
         
        //Convertendo bytes para string
        var authString = new  String(authDecode);

        //Separando usuario e senha, criando um array: [usuario, senha]
        String[]  credencials = authString.split(":");
        String username = credencials[0];      
        String password = credencials[1];

        System.out.println("Autorization: " );               
        System.out.println("Username: " + username );
        System.out.println("Password: " + password );
        
        //Validar usuario
       var user = this.userRepository.findByUsername(username);
           if(user == null ) {
                       response.sendError(401);
           
           }else{
                //Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                System.out.println(user.getUsername());
                System.out.println(user.getId());
                if (passwordVerify.verified) {
                    //Se estiver ok, deixa passar   
                    //Salvar o id do usuario na requisicao
                    request.setAttribute("idUser", user.getId());
                
                      filterChain.doFilter(request, response);
                } else {
                    System.out.println("Senha incorreta");
                    response.sendError(401);
                }
           }
         } else {
            //segue viagem
            filterChain.doFilter(request, response);
             }
      
        
    }   


}
