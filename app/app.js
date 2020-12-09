'use strict';
const express= require('express');
const app = express();
const mustache = require('mustache');
const filesystem = require('fs');
const comm_areas = require('./data/community_areas.json')
const port = Number(process.argv[2]);

const hbase = require('hbase')
var hclient = hbase({ host: process.argv[3], port: Number(process.argv[4])})

function counterToNumber(c) {
	return Number(Buffer.from(c).readBigInt64BE());
}

function unpackHbaseColumns(results) {

	let stats = {}

	results.forEach(function (item) {
		stats[item['key']] = {}
	});

	results.forEach(function (item) {
		let data = stats[item['key']]
		data[item['column'].split(':')[1]] = counterToNumber(item['$'])
	});

	return stats
}

function getFilteredResults(tableName, CommArea, res, template) {

	hclient.table(tableName).scan({
			filter: {
				type : "PrefixFilter",
				value: CommArea + ":"
			},
			maxVersions: 1},
		(err, cells) => {
			const rv = []
			for (const i in cells) {
				rv.push({
					type: cells[i]['key'].split(":")[1],
					calls: counterToNumber(cells[i]['$'])
				})
			}

			let html = mustache.render(template, {
				comm_area : comm_areas.COMMUNITY[CommArea],
				info : rv
			});
			res.send(html)
		})
}

app.use(express.static('public'));

// Redirect page to home when going to root url
app.get('/', function(r, res) {
	res.redirect('/home.html')
})

// Action for getting the average time delta between when a service request opens and when it closes
app.get('/avg_delta.html',function(req, res) {

	function computeAvgDelta(stats) {

		const rv = []

		for (const i in stats) {
			// calculate average time in seconds and convert to days
			let avg_delta = (stats[i]['delta'] / stats[i]['calls']) / 86400
			avg_delta = avg_delta.toFixed(2)
			if (avg_delta == 0) {
				avg_delta = "< 1"
			}
			rv.push({dept: i, avg_time: avg_delta})
		}

		return rv
	}

	hclient.table('mtrichardson_avg_delta_dept').scan((error, value) => {

		const data = computeAvgDelta(unpackHbaseColumns(value))
		const template = filesystem.readFileSync("mustache/avg_delta_dept.mustache").toString()
		const html = mustache.render(template, {
			info: data
		})

		res.send(html)
	})

})

// Action for getting the current number of open requests by community area
app.get('/open_requests.html',function(req, res) {

	hclient.table('mtrichardson_open_calls_dept').scan((error, value) => {

		const template = filesystem.readFileSync("mustache/open_requests.mustache").toString()

		for (const i in value) {
			value[i]['$'] = counterToNumber(value[i]['$'])
		}
		const html = mustache.render(template, {
			info: value
		})

		res.send(html)
	})

})

// Action for getting service request and crime totals by community area
app.get('/sr_crime_totals.html',function(req, res) {

	function buildSrCrimeTable(stats) {

		const rv = []

		for (const i in stats) {
			rv.push({
				comm_area: comm_areas['COMMUNITY'][i],
				calls: stats[i]['calls'],
				crimes: stats[i]['crimes']
			})
		}

		return rv
	}

	hclient.table('mtrichardson_crime_calls_by_comm_area').scan((error, value) => {

		const data = buildSrCrimeTable(unpackHbaseColumns(value))
		const template = filesystem.readFileSync("mustache/calls_crimes_table.mustache").toString()
		const html = mustache.render(template, {
			info: data
		})

		res.send(html)

	})

})

// Action for getting dropdown of community areas
app.get('/comm_area.html', function (req, res) {

	let option = req.query.stats
	let template = filesystem.readFileSync("mustache/comm_area_dropdown.mustache").toString();
	const data = []

	for (const i in comm_areas.COMMUNITY) {
		data.push({comm_area: i, name: comm_areas.COMMUNITY[i]})
	}

	let html = mustache.render(template, {
		crimes: option,
		names : data
	});

	res.send(html)

});

// Action for getting service requests totals by type for a given community area
app.get('/sr_type.html', function (req, res) {

	const comm_area = req.query['name']
	let template = filesystem.readFileSync("mustache/sr_type_by_comm.mustache").toString();
	getFilteredResults('mtrichardson_sr_type_by_comm', comm_area, res, template)
})

// Action for getting crime totals by type for a given community area
app.get('/crime_type.html', function (req, res) {

	const comm_area = req.query['name']
	let template = filesystem.readFileSync("mustache/crimes_by_comm.mustache").toString();
	getFilteredResults('mtrichardson_crime_by_comm', comm_area, res, template)
})
	
app.listen(port);
