import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../services/statistics.service';
import { Chart } from 'chart.js/auto';
import * as d3 from 'd3';

@Component({
  selector: 'app-company-statistics',
  templateUrl: './company-statistics.component.html',
  styleUrls: ['./company-statistics.component.css']
})
export class CompanyStatisticsComponent implements OnInit {

  companyName: string = "Orange"; 
  offersCount: number = 0;
  applicationsCount: number = 0;
  submissionToOfferRatio: number = 0;
  offersByDay: { [key: string]: number } = {};
  loading: boolean = true;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.loadCompanyStatistics();
  }

  loadCompanyStatistics(): void {
    this.loading = true;

    this.statisticsService.getOffersByCompanyRH(this.companyName).subscribe(data => {
      this.animateCountUp(data, 'offersCount');
    });

    this.statisticsService.getApplicationsByCompanyRH(this.companyName).subscribe(data => {
      this.animateCountUp(data, 'applicationsCount');
    });

    this.statisticsService.getSubmissionToOfferRatioByCompany(this.companyName).subscribe(data => {
      this.submissionToOfferRatio = data ?? 0;
      this.createGaugeChart(this.submissionToOfferRatio);
    });

    this.statisticsService.getOffersByCompanyPerDay(this.companyName).subscribe(data => {
      if (data && Object.keys(data).length > 0) {
        this.createLineChart(data);
      }
    });
  }

  // 🎯 Animation Compteur Progressif
  animateCountUp(targetValue: number, property: keyof this): void {
    let current = 0;
    const step = targetValue / 50;
    const interval = setInterval(() => {
      current += step;
      if (current >= targetValue) {
        current = targetValue;
        clearInterval(interval);
      }
      (this as any)[property] = Math.floor(current);
    }, 20);
  }

  // 📈 Graphique en ligne dynamique
  createLineChart(data: any): void {
    new Chart("lineChart", {
      type: 'line',
      data: {
        labels: Object.keys(data),
        datasets: [{
          label: "Evolution of Offers Over Time",
          data: Object.values(data),
          borderColor: '#E63946',
          borderWidth: 2,
          fill: true,
          backgroundColor: 'rgba(230, 57, 70, 0.2)'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: {
          duration: 2000,
          easing: 'easeOutBounce'
        }
      }
    });
  }

  // 🔥 **Gauge Chart dynamique pour le Ratio Candidatures/Offres**
  // 🔥 Correction complète pour afficher le Gauge Chart correctement
createGaugeChart(value: number): void {
  const width = 200, height = 120;
  const radius = 50;
  const startAngle = -Math.PI / 2;
  const endAngle = startAngle + (Math.PI * value / 100);

  // Supprimer le précédent SVG pour éviter les doublons
  d3.select("#gaugeChart").selectAll("*").remove();

  const svg = d3.select("#gaugeChart")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", `translate(${width / 2}, ${height / 1.2})`);

  // ✅ **Ajout du fond gris clair**
  svg.append("path")
    .attr("d", d3.arc()
      .innerRadius(radius - 10)
      .outerRadius(radius)
      .startAngle(startAngle)
      .endAngle(Math.PI / 2) as any)
    .attr("fill", "#eee");

  // ✅ **Ajout du segment coloré (jauge dynamique)**
  svg.append("path")
    .attr("d", d3.arc()
      .innerRadius(radius - 10)
      .outerRadius(radius)
      .startAngle(startAngle)
      .endAngle(endAngle) as any)
    .attr("fill", value > 50 ? "#28a745" : value > 30 ? "#ffc107" : "#dc3545");

  // ✅ **Ajout d’un cercle blanc au centre**
  svg.append("circle")
    .attr("cx", 0)
    .attr("cy", 0)
    .attr("r", radius - 20)
    .attr("fill", "white");

  // ✅ **Ajout du texte du ratio (%) au centre**
  svg.append("text")
    .attr("x", 0)
    .attr("y", 5)
    .attr("text-anchor", "middle")
    .attr("font-size", "16px")
    .attr("font-weight", "bold")
    .attr("fill", "#333")
    .text(value.toFixed(1) + "%");
}
animateRatio(value: number): void {
  const width = 120, height = 80;
  const radius = 40;

  // ✅ Multiplier par 100 pour l'affichage correct
  const percentage = value * 100; 

  // ✅ Sélectionner l'élément SVG
  d3.select("#gaugeChart").selectAll("*").remove(); // 🔄 Reset avant de redessiner

  const svg = d3.select("#gaugeChart")
    .attr("width", width)
    .attr("height", height)
    .append("g")
    .attr("transform", `translate(${width / 2}, ${height / 1.5})`);

  // ✅ Fond gris (base)
  svg.append("path")
    .attr("d", d3.arc()
      .innerRadius(radius - 10)
      .outerRadius(radius)
      .startAngle(-Math.PI / 2)
      .endAngle(Math.PI / 2) as any)
    .attr("fill", "#ddd");

  // ✅ Couleur dynamique en fonction du ratio
  const arc = d3.arc()
    .innerRadius(radius - 10)
    .outerRadius(radius)
    .startAngle(-Math.PI / 2)
    .endAngle(-Math.PI / 2 + Math.PI * (percentage / 100)); // ✅ Correction ici

  svg.append("path")
    .attr("d", arc as any)
    .attr("fill", percentage > 50 ? "green" : percentage > 30 ? "orange" : "red");

  // ✅ Ajouter le texte du ratio au centre
  svg.append("text")
    .attr("x", 0)
    .attr("y", 10)
    .attr("text-anchor", "middle")
    .attr("font-size", "14px")
    .attr("fill", "#333")
    .text(percentage.toFixed(1) + "%");
}

}