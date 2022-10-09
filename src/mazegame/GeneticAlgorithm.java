package mazegame;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a genetic pool of maze solvers.
 * The genetic algorithm is a population of members — each member has
 * a chromosome that represents its solution to the maze. The fittest
 * (best at solving the maze) members of the population are breeded
 * and mutated to form a new population which is better at solving the
 * maze than the previous one.
 */
class GeneticAlgorithm {
  private int moveLimit; // represents the size of each chomosome
  private int populationSize; // represents the number of member
  private double mutationChance; // represents the probability of a gene changing while mutating the chromosome
  private Random randomizer;
  private ArrayList<Member> population;

  final int SURVIVAL_RATE = 5; // the proportion of unfit population to kill during each generation

  /**
   * Constructs this GeneticAlgorithm with the given params
   * @param moveLimit the size of each chromosome
   * @param populationSize the number of members in the population
   * @param mutationChance the probability of a gene changing between generations
   */
  public GeneticAlgorithm(int moveLimit, int populationSize, double mutationChance) {
    this.moveLimit = moveLimit;
    this.populationSize = populationSize;
    this.mutationChance = mutationChance;
    this.randomizer = new Random();
    this.populate();
  }

  /**
   * Populates the gene pool with (populationSize amount of) members.
   */
  void populate() {
    this.population = new ArrayList<Member>();
    for (int i = 0; i < this.populationSize; i++) {
      ArrayList<Integer> chromosome = new ArrayList<Integer>();

      for (int j = 0; j < this.moveLimit; j++) {
        chromosome.add(this.randomizer.nextInt(4) + 1);
      }

      population.add(new Member(chromosome));
    }
  }

  /**
   * Determines the fittest member in this gene pool.
   * @param maze the maze that the members are solving
   * @return the fittest member of this gene pool
   */
  Member fittestMember(Maze maze) {
    Utils u = new Utils();

    u.quicksort(this.population, new MemberComparator(maze));

    return this.population.get(0);
  }

  /**
   * Breeds the top 1/SURVIVAL_RATE and adds one new random member.
   * Assume this population is already sorted by fitness.
   * @param maze the maze that the members are solving
   */
  void crossOver(Maze maze) {
    ArrayList<Member> newPopulation = new ArrayList<Member>();

    ArrayList<Integer> chromosome = new ArrayList<Integer>();

    for (int j = 0; j < this.moveLimit; j++) {
      chromosome.add(this.randomizer.nextInt(4) + 1);
    }

    population.add(0, new Member(chromosome));

    for (int i = 0; i < this.populationSize; i++) {
      int swapOver = this.randomizer.nextInt(this.moveLimit + 1);
      Member newMember = new Member(
              this.population.get(this.randomizer.nextInt(this.populationSize) / this.SURVIVAL_RATE).chromosome);
      newMember.chromosome = new ArrayList<Integer>(newMember.chromosome.subList(0, swapOver));
      newMember.chromosome
              .addAll(this.population.get(this.randomizer.nextInt(this.populationSize / this.SURVIVAL_RATE)).chromosome
                      .subList(swapOver, this.moveLimit));
      this.mutate(newMember.chromosome);
      newPopulation.add(newMember);
    }

    this.population = newPopulation;
  }

  /**
   * Randomly mutates a given member of this population.
   * @param chromosome the chromosome of the member to be mutated
   */
  void mutate(ArrayList<Integer> chromosome) {
    if (this.mutationChance > Math.random()) {
      chromosome.set(this.randomizer.nextInt(this.moveLimit), this.randomizer.nextInt(4) + 1);
    }

    if (this.mutationChance > Math.random()) {
      this.mutate(chromosome);
    }
  }
}