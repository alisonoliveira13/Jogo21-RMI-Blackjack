package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.models.Carta;
import rmi.models.EstadoJogo;



public interface JogoRMI extends Remote {

    String conectar(String nomeJogador) throws RemoteException;
    EstadoJogo iniciarRodada(String nomeJogador) throws RemoteException;
    EstadoJogo pedirCarta(String nomeJogador) throws RemoteException;
    EstadoJogo parar(String nomeJogador) throws RemoteException;
}
