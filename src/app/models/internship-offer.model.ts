// ✅ Interface pour la météo reçue depuis OpenWeatherMap
export interface WeatherData {
  main: {
    temp: number;
  };
  weather: {
    description: string;
    icon: string;
  }[];
  name: string;
}

// ✅ Correction : utilisation stricte des enums pour la robustesse du code
export type StageType = 'PFE' | 'SUMMER';
export type OfferStatus = 'OPEN' | 'CLOSED' | 'ARCHIVED';

export interface InternshipOffer {
  id: number;
  title: string;
  companyName: string;
  location: string;
  duration: number;
  stageType: StageType; // ✅ utilisation du type StageType
  offerStatus: OfferStatus; // ✅ utilisation du type OfferStatus
  remote: boolean;
  paid: boolean;
  startDate: string;
  creationDate: string;
  jobDescription: string;
  imageUrls: string[];

  // ✅ Ajout de la météo
  weather?: WeatherData;

  // ✅ Ajout pour support Leaflet
  lat?: number;
  lng?: number;
}



