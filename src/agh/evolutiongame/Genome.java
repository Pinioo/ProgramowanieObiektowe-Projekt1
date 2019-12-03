package agh.evolutiongame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Genome {
    private final int[] genes = new int[32];

    public Genome(){
        //Providing that genome will be complete
        for(int i = 0; i < 8; ++i)
            this.genes[i] = i;

        //Randomizing rest
        Random rand = new Random();
        for(int i = 8; i < 32; ++i)
            this.genes[i] = rand.nextInt(8);

        Arrays.sort(genes);
    }

    public Genome(Animal parent1, Animal parent2){
        int[] groups = new int[32];
        Random rand = new Random();

        //Setting one of three groups to every gene
        for(int i = 0; i < 32; ++i)
            groups[i] = rand.nextInt(3);

        //Group 0 genes inherit from parent1
        //Groups 1 and 2 genes inherit from parent2
        for(int i = 0; i < 32; ++i){
            if(groups[i] == 0)
                this.genes[i] = parent1.getGenome().getGene(i);
            else
                this.genes[i] = parent2.getGenome().getGene(i);
        }

        //Providing that genome will be complete by adding not existing genes in random places
        LinkedList<Integer> lackingList = this.lackingGenes();
        while(!lackingList.isEmpty()){
            for(int gene: lackingList){
                this.genes[rand.nextInt(32)] = gene;
            }
            lackingList = this.lackingGenes();
        }
    }

    private LinkedList<Integer> lackingGenes(){
        LinkedList<Integer> result = new LinkedList<>();
        boolean[] existingGenes = new boolean[8];
        for (int i = 0; i < 8; i++)
            existingGenes[i] = false;

        //Checking which genes are present
        for(int g : this.genes){
            existingGenes[g] = true;
        }

        //Appending non-existing genes
        for(int i = 0; i < 8; i++) {
           if(!existingGenes[i])
               result.add(i);
        }

        return result;
    }

    public int getGene(int i){
        return this.genes[i];
    }

    public int getRandomGene(){
        return this.genes[(new Random()).nextInt(32)];
    }
}
