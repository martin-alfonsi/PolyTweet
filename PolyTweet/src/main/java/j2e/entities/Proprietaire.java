package j2e.entities;

import j2e.application.NotAllowedException;

public class Proprietaire extends Moderateur {

	public Proprietaire(String login) {
		super(login);
		
	}
	
	@Override
	void ajouterModerateur(Moderateur moderateur){
		canalCourant.ajouterModerateur(moderateur);
	}
	
	@Override
	void supprimerModerateur(Moderateur moderateur){
		canalCourant.supprimerModerateur(moderateur);
	}
	

}