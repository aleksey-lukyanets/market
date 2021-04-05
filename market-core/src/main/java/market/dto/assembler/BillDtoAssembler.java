package market.dto.assembler;

import market.domain.Bill;
import market.dto.BillDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class BillDtoAssembler implements RepresentationModelAssembler<Bill, BillDTO> {

	@Override
	public BillDTO toModel(Bill bill) {
		BillDTO dto = new BillDTO();
		dto.setId(bill.getId());
		dto.setNumber(bill.getNumber());
		dto.setDateCreated(bill.getDateCreated().toString());
		dto.setTotalCost(bill.getTotalCost());
		dto.setPayed(bill.isPayed());
		dto.setCcNumber(bill.getCcNumber());
		return dto;
	}
}
