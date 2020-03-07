package market.dto.assembler;

import market.controller.backend.DistilleryController;
import market.domain.Distillery;
import market.dto.DistilleryDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class DistilleryDtoAssembler extends RepresentationModelAssemblerSupport<Distillery, DistilleryDTO> {

	public DistilleryDtoAssembler() {
		super(DistilleryController.class, DistilleryDTO.class);
	}

	@Override
	public DistilleryDTO toModel(Distillery distillery) {
		DistilleryDTO dto = createModelWithId(distillery.getId(), distillery);
		dto.setId(distillery.getId());
		dto.setTitle(distillery.getTitle());
		dto.setDescription(distillery.getDescription());
		dto.setRegion(distillery.getRegion().getName());
		return dto;
	}

	public Distillery toDomain(DistilleryDTO dto, long distilleryId) {
		return new Distillery.Builder()
			.setId(distilleryId)
			.setTitle(dto.getTitle())
			.setDescription(dto.getDescription())
			.build();
	}
}
