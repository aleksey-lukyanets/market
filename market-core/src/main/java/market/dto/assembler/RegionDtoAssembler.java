package market.dto.assembler;

import market.domain.Region;
import market.dto.RegionDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import java.util.List;

public class RegionDtoAssembler implements RepresentationModelAssembler<Region, RegionDTO> {

	@Override
	public RegionDTO toModel(Region region) {
		RegionDTO dto = new RegionDTO();
		dto.setId(region.getId());
		dto.setName(region.getName());
		dto.setSubtitle(region.getSubtitle());
		dto.setColor(region.getColor());
		dto.setDescription(region.getDescription());
		return dto;
	}

	public RegionDTO[] toDtoArray(List<Region> items) {
		return toCollectionModel(items).getContent().toArray(new RegionDTO[items.size()]);
	}

	public Region toDomain(RegionDTO dto) {
		return new Region.Builder()
			.setId(dto.getId())
			.setName(dto.getName())
			.setSubtitle(dto.getSubtitle())
			.setColor(dto.getColor())
			.setDescription(dto.getDescription())
			.build();
	}
}
