export interface CategoriaProfile {
  label: string;
  value: string;
}

export const categoriaProfileOptions: CategoriaProfile[] = [
  { label: 'Carro pequeno', value: 'driving-car' },
  { label: 'Veículos pesados', value: 'driving-hgv' },
  { label: 'Bicicleta regular', value: 'cycling-regular' },
  { label: 'Bicicleta elétrica', value: 'cycling-electric' },
  { label: 'Caminhada', value: 'foot-walking' },
];
