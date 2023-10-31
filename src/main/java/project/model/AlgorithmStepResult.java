package project.model;

public enum AlgorithmStepResult {
	// Algorithm is still trying to find a path
	CONTINUING,
	// Algorithm found path
	FOUND,
	// Algorithm can't find a path from snake head to FOOD
	TRAPPED
}
