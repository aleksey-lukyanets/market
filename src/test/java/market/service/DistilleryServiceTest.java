package market.service;

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
	private static final long REGION_ID = 123L;
	private static final String REGION_NAME = "region_name";
	private static final long DISTILLERY_ID = 234L;
	private static final String DISTILLERY_TITLE = "distillery_title";
	private static final String DISTILLERY_DESCRIPTION = "distillery_description";

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
		region = new Region.Builder()
			.setId(REGION_ID)
			.setName(REGION_NAME)
			.build();
		distillery = new Distillery.Builder()
			.setId(DISTILLERY_ID)
			.setRegion(region)
			.setTitle(DISTILLERY_TITLE)
			.setDescription(DISTILLERY_DESCRIPTION)
			.build();
		distilleryService = new DistilleryServiceImpl(regionService, distilleryDAO);
	}

	@Test
	public void findAll() {
		when(distilleryDAO.findAll()).thenReturn(Collections.singletonList(distillery));

		List<Distillery> retrieved = distilleryService.findAll();

		assertThat(retrieved, contains(distillery));
	}

	@Test
	public void findByRegion() {
		when(distilleryDAO.findByRegionOrderByTitleAsc(any(Region.class))).thenReturn(Collections.singletonList(distillery));

		List<Distillery> retrieved = distilleryService.findByRegion(region);

		assertThat(retrieved, contains(distillery));
	}

	@Test
	public void findById() {
		when(distilleryDAO.findById(DISTILLERY_ID)).thenReturn(Optional.of(distillery));

		Distillery retrieved = distilleryService.findById(DISTILLERY_ID);

		assertThat(retrieved, equalTo(distillery));
	}

	@Test
	public void findByTitle() {
		when(distilleryDAO.findByTitle(DISTILLERY_TITLE)).thenReturn(distillery);

		Distillery retrieved = distilleryService.findByTitle(DISTILLERY_TITLE);

		assertThat(retrieved, equalTo(distillery));
	}

	@Test
	public void create() {
		when(regionService.findByName(REGION_NAME)).thenReturn(region);

		distilleryService.create(distillery, REGION_NAME);

		verify(distilleryDAO).save(distilleryCaptor.capture());
		assertThat(distilleryCaptor.getValue(), equalTo(distillery));
	}

	@Test
	public void update() {
		Distillery changedDistillery = new Distillery.Builder()
			.setId(distillery.getId())
			.setRegion(region)
			.setTitle(DISTILLERY_TITLE + "_changed")
			.setDescription(DISTILLERY_DESCRIPTION + "_changed")
			.build();
		when(regionService.findByName(region.getName())).thenReturn(region);
		when(distilleryDAO.findById(distillery.getId())).thenReturn(Optional.of(distillery));

		distilleryService.update(changedDistillery, REGION_NAME);

		verify(distilleryDAO).save(distilleryCaptor.capture());
		assertThat(distilleryCaptor.getValue(), equalTo(changedDistillery));
	}

	@Test
	public void delete() {
		distilleryService.delete(DISTILLERY_ID);

		verify(distilleryDAO).deleteById(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(DISTILLERY_ID));
	}
}
