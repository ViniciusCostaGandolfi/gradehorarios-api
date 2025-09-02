import enum

class SolverStatus(enum.Enum):
    SOLVED = "SOLVED"
    OPTIMAL = "OPTIMAL"
    FEASIBLE = "FEASIBLE"
    INFEASIBLE = "INFEASIBLE"