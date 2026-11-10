package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myapp.mymodel.Country
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CountriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                val countries by viewModel.filteredCountries.collectAsState()
                val regions by viewModel.regions.collectAsState()
                val nameFilter by viewModel.nameFilter.collectAsState()
                val regionFilter by viewModel.regionFilter.collectAsState()
                val favourite by viewModel.favourite.collectAsState()
                val error by viewModel.error.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Countries",
                            style = MaterialTheme.typography.headlineMedium
                        )


                        Text(
                            text = "My favourite: ${favourite ?: "none yet"}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )


                        FiltersSection(
                            nameFilter = nameFilter,
                            onNameFilterChange = viewModel::onNameFilterChange,
                            regions = regions,
                            selectedRegion = regionFilter,
                            onRegionSelected = viewModel::onRegionSelected
                        )

                        if (error != null) {
                            Text(text = error!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text(
                                text = "${countries.size} countries",
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        CountriesList(
                            countries = countries,
                            favourite = favourite,
                            onVote = viewModel::vote
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FiltersSection(
    nameFilter: String,
    onNameFilterChange: (String) -> Unit,
    regions: List<String>,
    selectedRegion: String?,
    onRegionSelected: (String?) -> Unit
) {
    Column {

        OutlinedTextField(
            value = nameFilter,
            onValueChange = onNameFilterChange,
            label = { Text("Name starts with...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedRegion == null,
                    onClick = { onRegionSelected(null) },
                    label = { Text("All") }
                )
            }
            items(regions) { region ->
                FilterChip(
                    selected = selectedRegion == region,
                    onClick = { onRegionSelected(region) },
                    label = { Text(region) }
                )
            }
        }
    }
}

@Composable
fun CountriesList(
    countries: List<Country>,
    favourite: String?,
    onVote: (Country) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(countries, key = { it.name }) { country ->
            CountryCard(
                country = country,
                isFavourite = country.name == favourite,
                onVote = { onVote(country) }
            )
        }
    }
}

@Composable
fun CountryCard(country: Country, isFavourite: Boolean, onVote: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = country.flags.png,
                contentDescription = "Flag of ${country.name}",
                modifier = Modifier.weight(1f)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(text = country.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Capital: ${country.capital ?: "No capital"}")
                Text(text = "Region: ${country.region}")
                Text(text = "Population: ${country.population}")


                if (isFavourite) {
                    Button(onClick = onVote) { Text("★ My favourite") }
                } else {
                    OutlinedButton(onClick = onVote) { Text("Vote") }
                }
            }
        }
    }
}
