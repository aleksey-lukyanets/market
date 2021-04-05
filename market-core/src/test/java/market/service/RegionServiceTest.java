package market.service;

import market.FixturesFactory;
import market.dao.RegionDAO;
import market.domain.Region;
import market.service.impl.RegionServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

	@Mock
	private RegionDAO regionDAO;

	@Captor
	private ArgumentCaptor<Region> regionCaptor;
	@Captor
	private ArgumentCaptor<Long> longCaptor;

	private RegionService regionService;
	private Region region;

	@BeforeEach
	public void setUp() {
		region = FixturesFactory.region().build();
		regionService = new RegionServiceImpl(regionDAO);
	}

	@Test
	public void findAll() {
		when(regionDAO.findAll())
			.thenReturn(Collections.singletonList(region));

		List<Region> retrieved = regionService.findAll();

		assertThat(retrieved, contains(region));
	}

	@Test
	public void findOne() {
		when(regionDAO.findById(region.getId()))
			.thenReturn(Optional.of(region));

		Region retrieved = regionService.findOne(region.getId());

		assertThat(retrieved, equalTo(region));
	}

	@Test
	public void findByName() {
		when(regionDAO.findByName(region.getName()))
			.thenReturn(Optional.of(region));

		Region retrieved = regionService.findByName(region.getName());

		assertThat(retrieved, equalTo(region));
	}

	@Test
	public void create() {
		regionService.create(region);

		verify(regionDAO).save(regionCaptor.capture());
		assertThat(regionCaptor.getValue(), equalTo(region));
	}

	@Test
	public void update() {
		Region changedRegion = new Region.Builder(region)
			.setName(region.getName() + "_changed")
			.build();
		when(regionDAO.findById(region.getId()))
			.thenReturn(Optional.of(region));

		regionService.update(changedRegion.getId(), changedRegion);

		verify(regionDAO).save(regionCaptor.capture());
		assertThat(regionCaptor.getValue(), equalTo(changedRegion));
	}

	@Test
	public void delete() {
		regionService.delete(region.getId());

		verify(regionDAO).deleteById(longCaptor.capture());
		assertThat(longCaptor.getValue(), equalTo(region.getId()));
	}
}
