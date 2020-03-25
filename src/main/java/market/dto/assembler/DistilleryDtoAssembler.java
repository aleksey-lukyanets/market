package market.dto.assembler;

import market.controller.backend.DistilleryController;
import market.domain.Distillery;
import market.dto.DistilleryDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;

public class DistilleryDtoAssembler extends RepresentationModelAssemblerSupport<Distillery, DistilleryDTO> {

	public DistilleryDtoAssembler() {
		super(DistilleryController.class, DistilleryDTO.class);
	}

	@Override
	public DistilleryDTO toModel(Distillery distillery) {
		DistilleryDTO dto = instantiateModel(distillery);
		dto.setId(distillery.getId());
		dto.setTitle(distillery.getTitle());
		dto.setDescription(distillery.getDescription());
		dto.setRegion(distillery.getRegion().getName());
		return dto;
	}

	public DistilleryDTO[] toDtoArray(List<Distillery> items) {
		return toCollectionModel(items).getContent().toArray(new DistilleryDTO[items.size()]);
	}

	public Distillery toDomain(DistilleryDTO dto) {
		return new Distillery.Builder()
			.setTitle(dto.getTitle())
			.setDescription(dto.getDescription())
			.build();
	}
}
