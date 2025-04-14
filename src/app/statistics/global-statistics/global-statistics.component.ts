import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js/auto';
import { StatisticsService } from '../../services/statistics.service';
import { StatsDTO, StatsService } from 'src/app/services/stats.service';

@Component({
  selector: 'app-global-statistics',
  templateUrl: './global-statistics.component.html',
  styleUrls: ['./global-statistics.component.css']
})
export class GlobalStatisticsComponent implements OnInit {
  stats!: StatsDTO;

  constructor(private statisticsService: StatisticsService,private statsService: StatsService) {}

  ngOnInit(): void {
    this.loadStatistics();
    setTimeout(() => {
      window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
    }, 1000); // Attendre 1 seconde après le chargement
    this.statsService.getStats().subscribe((data) => {
      this.stats = data;
    });
  }
  

  loadStatistics() {
    this.statisticsService.getOffersByCompany().subscribe(data => {
      this.createBarChart(data, 'barChart', 'Number of Offers per Company');
    });

    this.statisticsService.getOffersByDay().subscribe(data => {
      this.createLineChart(data, 'lineChart', 'Evolution of Offers Over Time');
    });

    this.statisticsService.getOffersByLocation().subscribe(data => {
      this.createPieChart(data, 'pieChart', 'Geographical Distribution of Offers');
    });

    this.statisticsService.getSubmissionToOfferRatio().subscribe(data => {
      this.createGaugeChart(data, 'gaugeChart', 'Application-to-Offer Ratio Indicator');
    });
  }

  createBarChart(data: any, canvasId: string, title: string) {
    const ctx = document.getElementById(canvasId) as HTMLCanvasElement;
    if (!ctx) return;
  
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: Object.keys(data),
        datasets: [{
          label: title,
          data: Object.values(data),
          backgroundColor: ['#ffcc00', '#ff5733', '#33ff57', '#3357ff'],
          borderWidth: 1
        }]
      },
      options: {
        plugins: {
          legend: { display: false },
          tooltip: { enabled: true },
        }
      }
    });
  }
  
  generateRandomColors(length: number): string[] {
    const colors: string[] = [];
    for (let i = 0; i < length; i++) {
        const color = `hsl(${Math.floor(Math.random() * 360)}, 70%, 50%)`; // Génère une couleur HSL aléatoire
        colors.push(color);
    }
    return colors;
}

  createLineChart(data: any, canvasId: string, title: string) {
    new Chart(canvasId, {
      type: 'line',
      data: {
        labels: Object.keys(data),
        datasets: [{
          label: title,
          data: Object.values(data),
          borderColor: '#ff5733',
          borderWidth: 2,
          fill: false
        }]
      }
    });
  }

  createPieChart(data: any, canvasId: string, title: string) {
    const labels = Object.keys(data);
    const values = Object.values(data);
    const colors = this.generateRandomColors(labels.length); // Génère des couleurs uniques

    new Chart(canvasId, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: title,
                data: values,
                backgroundColor: colors // Utilise les couleurs générées
            }]
        }
    });
}


  createGaugeChart(value: number, canvasId: string, title: string) {
    let progress = 0;
    const interval = setInterval(() => {
      progress += 1;
      if (progress >= value) clearInterval(interval);
      
      new Chart(canvasId, {
        type: 'doughnut',
        data: {
          datasets: [{
            data: [progress, 100 - progress],
            backgroundColor: ['#33ff57', '#ddd']
          }]
        }
      });
    }, 10); // Animation rapide du ratio
  }
}  
