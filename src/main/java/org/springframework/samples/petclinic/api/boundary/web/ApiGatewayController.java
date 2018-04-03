
package org.springframework.samples.petclinic.api.boundary.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.api.application.CustomersServiceClient;
import org.springframework.samples.petclinic.api.application.OwnerDetails;
import org.springframework.samples.petclinic.api.application.VisitDetails;
import org.springframework.samples.petclinic.api.application.VisitsServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;


@RestController
@RequiredArgsConstructor
public class ApiGatewayController {

    private final CustomersServiceClient customersServiceClient = new CustomersServiceClient();

    private final VisitsServiceClient visitsServiceClient = new VisitsServiceClient();

    @GetMapping(value = "owners/{ownerId}")
    public OwnerDetails getOwnerDetails(final @PathVariable int ownerId) {
        final OwnerDetails owner = customersServiceClient.getOwner(ownerId);
        supplyVisits(owner, visitsServiceClient.getVisitsForPets(owner.getPetIds(), ownerId));
        return owner;
    }
    
//    @GetMapping(value = "/api/owners/{ownerId}")
//    public OwnerDetails getOwnerDetails(final @PathVariable int ownerId) {
//        final OwnerDetails owner = customersServiceClient.getOwner(ownerId);
//        supplyVisits(owner, visitsServiceClient.getVisitsForPets(owner.getPetIds(), ownerId));
//        return owner;
//    }
    
//    @GetMapping(value = "/api/customer/owners")
//    public OwnerDetails[] getOwners() {
//        System.out.println("JUST GOT A CALL FROM CLENT");
//        final OwnerDetails[] owners = customersServiceClient.getOwners();
//        return owners;
//    }

    
    private void supplyVisits(final OwnerDetails owner, final Map<Integer, List<VisitDetails>> visitsMapping) {
        owner.getPets().forEach(pet ->
            pet.getVisits().addAll(Optional.ofNullable(visitsMapping.get(pet.getId())).orElse(emptyList())));
    }
}
