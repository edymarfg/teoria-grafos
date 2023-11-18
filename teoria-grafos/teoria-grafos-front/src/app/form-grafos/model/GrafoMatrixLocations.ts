export interface GrafoMatrixLocations {
  inicio: string;
  fim: PesoGrafoMatrix[];
}

export interface PesoGrafoMatrix {
  fim: string;
  duration: number;
  distance: number;
}
