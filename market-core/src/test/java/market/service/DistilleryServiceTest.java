package market.service;

import market.FixturesFactory;
import market.dao.DistilleryDAO;
import market.domain.Distillery;
import market.domain.Region;
import market.service.impl.DistilleryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistilleryServiceTest {

	@Mock
	private DistilleryDAO distilleryDAO;
	@Mock
	private RegionService regionService;

	@Captor
	private ArgumentCaptor<Distillery> distilleryCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private DistilleryService distilleryService;
	private Distillery distillery;
	private Region region;

	@BeforeEach
	public void setUp() {
		region = FixturesFactory.region().build();
		distillery = FixturesFactory.distillery(region).build();
		distilleryService = new DistilleryServiceImpl(regionService, distilleryDAO);
	}

	@Test
	public void findAll() {
		when(distilleryDAO.findAll())
			.thenReturn(Collections.singletonList(distillery));

		List<Distillery> retrieved = distilleryService.findAll();

		assertThat(retrieved, contains(distillery));
	}

	@Test
	public void findByRegion() {
		when(distilleryDAO.findByRegionOrderByTitleAsc(any(Region.class)))
			.thenReturn(Collections.singletonList(distillery));

		List<Distillery> retrieved = distilleryService.findByRegion(region);

		assertThat(retrieved, contains(distillery));
	}

	@Test
	public void findById() {
		when(distilleryDAO.findById(distillery.getId()))
			.thenReturn(Optional.of(distillery));

		Distillery retrieved = distilleryService.findById(distillery.getId());

		assertThat(retrieved, equalTo(distillery));
	}

	@Test
	public void findByTitle() {
		when(distilleryDAO.findByTitle(distillery.getTitle()))
			.thenReturn(distillery);

		Distillery retrieved = distilleryService.findByTitle(distillery.getTitle());

		assertThat(retrieved, equalTo(distillery));
	}

	@Test
	public void create() {
		when(regionService.findByName(region.getName()))
			.thenReturn(region);

		distilleryService.create(distillery, region.getName());

		verify(distilleryDAO).save(distilleryCaptor.capture());
		assertThat(distilleryCaptor.getValue(), equalTo(distillery));
	}

	@Test
	public void update() {
		Distillery changedDistillery = new Distillery.Builder(distillery)
			.setTitle(distillery.getTitle() + "_changed")
			.setDescription(distillery.getDescription() + "_changed")
			.build();
		when(regionService.findByName(region.getName()))
			.thenReturn(region);
		when(distilleryDAO.findById(distillery.getId()))
			.thenReturn(Optional.of(distillery));

		distilleryService.update(distillery.getId(), changedDistillery, distillery.getRegion().getName());

		verify(distilleryDAO).save(distilleryCaptor.capture());
		assertThat(distilleryCaptor.getValue(), equalTo(changedDistillery));
	}

	@Test
	public void delete() {
		distilleryService.delete(distillery.getId());

		verify(distilleryDAO).deleteById(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(distillery.getId()));
	}
}
