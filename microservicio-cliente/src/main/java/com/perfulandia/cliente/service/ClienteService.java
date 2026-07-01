package com.perfulandia.cliente.service;

import com.perfulandia.cliente.model.Cliente;
import com.perfulandia.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente createCliente(Cliente cliente) {
        cliente.setFechaRegistro(LocalDate.now());
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> getClienteById(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Cliente> updateCliente(Long id, Cliente clienteDetails) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        
        if (optionalCliente.isPresent()) {
            Cliente existingCliente = optionalCliente.get();
            if (clienteDetails.getNombre() != null) existingCliente.setNombre(clienteDetails.getNombre());
            if (clienteDetails.getApellido() != null) existingCliente.setApellido(clienteDetails.getApellido());
            if (clienteDetails.getTelefono() != null) existingCliente.setTelefono(clienteDetails.getTelefono());
            if (clienteDetails.getDireccion() != null) existingCliente.setDireccion(clienteDetails.getDireccion());
            
            return Optional.of(clienteRepository.save(existingCliente));
        }
        return Optional.empty();
    }

    public boolean deleteCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return true;
        }
        return false;
    }
}
