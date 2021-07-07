package pe.edu.utp.electroplus.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.electroplus.model.Usuario;
import pe.edu.utp.electroplus.model.Role;
import pe.edu.utp.electroplus.repository.ClienteRepository;
import pe.edu.utp.electroplus.repository.RoleRepository;
import pe.edu.utp.electroplus.service.ClienteService;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder cryptPasswordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> users = clienteRepository.findAll();
        users.forEach(u -> u.setCodigoRole(u.getRole().getCodigo()));
        return users;
    }

    @Override
    public void guardar(Usuario usuario) {
        usuario.setPasswordencode(cryptPasswordEncoder.encode(usuario.getPassword()));
        String[] cr = usuario.getCodigoRole().split(",");
        usuario.setRole(roleRepository.findByCodigo(cr[cr.length - 1]));
        clienteRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public List<Role> listarRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Usuario findByUsername(String username) {
        Usuario user = clienteRepository.findByUsername(username);
        if (Objects.nonNull(user)) user.setCodigoRole(user.getRole().getCodigo());
        return user;
    }
}
