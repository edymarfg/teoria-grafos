export interface Step {
  distance: number;
  duration: number;
  type: number;
  instruction: string;
  name: string;
  exit_number: number;
  way_points: number[];
}
