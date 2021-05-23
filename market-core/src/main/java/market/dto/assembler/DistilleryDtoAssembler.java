package market.dto.assembler;

import market.domain.Distillery;
import market.dto.DistilleryDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;

public class DistilleryDtoAssembler implements RepresentationModelAssembler<Distillery, DistilleryDTO> {

	@Override
	public DistilleryDTO toModel(Distillery distillery) {
		DistilleryDTO dto = new DistilleryDTO();
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
