package market.dto.assembler;

import market.controller.backend.RegionController;
import market.domain.Region;
import market.dto.RegionDTO;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RegionDtoAssembler extends RepresentationModelAssemblerSupport<Region, RegionDTO> {

	public RegionDtoAssembler() {
		super(RegionController.class, RegionDTO.class);
	}

	@Override
	public RegionDTO toModel(Region region) {
		RegionDTO dto = createModelWithId(region.getId(), region);
		dto.setId(region.getId());
		dto.setName(region.getName());
		dto.setSubtitle(region.getSubtitle());
		dto.setColor(region.getColor());
		dto.setDescription(region.getDescription());
		return dto;
	}

	public Region toDomain(RegionDTO dto) {
		Region region = new Region();
		region.setId(dto.getId());
		region.setName(dto.getName());
		region.setSubtitle(dto.getSubtitle());
		region.setColor(dto.getColor());
		region.setDescription(dto.getDescription());
		return region;
	}
}
