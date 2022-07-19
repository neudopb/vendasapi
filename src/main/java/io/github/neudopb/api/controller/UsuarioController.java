package io.github.neudopb.api.controller;

import io.github.neudopb.api.dto.CredenciaisDTO;
import io.github.neudopb.api.dto.TokenDTO;
import io.github.neudopb.domain.entity.Usuario;
import io.github.neudopb.exception.SenhaInvalidaException;
import io.github.neudopb.security.jwt.JwtService;
import io.github.neudopb.service.impl.UsuarioServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Api("API Usuarios")
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cadastrar usuário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Usuário cadastrado com sucesso"),
            @ApiResponse(code = 404, message = "Erro de validação")
    })
    public Usuario salvar(@RequestBody @Valid Usuario usuario) {
        String senhaCript = encoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCript);

        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    @ApiOperation("Autenticar Usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário autenticado com sucesso"),
            @ApiResponse(code = 404, message = "Usuário não encontrado")
    })
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .username(credenciais.getUsername())
                    .password(credenciais.getPassword())
                    .build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getUsername(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
