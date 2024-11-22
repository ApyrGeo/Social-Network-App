package map.utils;

import map.domain.Tuple;

import java.util.*;

public class Graph {

    /**
     * Depth First Search Algorithm for finding a Connected Component
     * @param nodes - map representing the Adjacency List
     * @param visited - map representing the visited nodes
     * @param currentNode - current node in the search
     * @param componentNr - number of the component (number that should be put in the visited map as the status)
     */
    public void DFS(Map<Long, ArrayList<Long>> nodes, Map<Long, Integer> visited, Long currentNode, Integer componentNr) {
        visited.put(currentNode, componentNr);
        nodes.get(currentNode).forEach(n -> {
            if (visited.get(n) == 0) {
                DFS(nodes, visited, n, componentNr);
            }
        });
    }

    /**
     * Breadth First Search Algorithm for fining the longest path starting from a given node
     * @param nodes - map representing the Adjacency List
     * @param visited - map representing the visited nodes - in this case it will be used as a parents map
     * @param startingNode - node where the BFS starts from
     * @return list of id`s representing the nodes from the largest path, starting from startingNode
     */
    public List<Long> BFS(Map<Long, ArrayList<Long>> nodes, Map<Long, Integer> visited, Long startingNode) {
        Queue<Long> queue = new LinkedList<>();
        queue.add(startingNode);

        //change the role for the visited map: parent map
        visited.forEach((k,v) -> visited.put(k, -2));

        int lastParent = -1;
        long keyLastNode = -1L;

        visited.put(startingNode, -1);
        //basic BFS for populating the parents map and searching the deepest node
        while (!queue.isEmpty()) {
            Long currentNode = queue.remove();

            //add to queue all the adjacent nodes, that are not in queue already
            nodes.get(currentNode).forEach(n -> {
                if(visited.get(n) == -2 && !queue.contains(n)){
                    queue.add(n);
                    //set the parent for found node n as current node
                    visited.put(n, Math.toIntExact(currentNode));
                }
            });

            //saves the last node so we can go back from the deepest node to the first one
            keyLastNode = currentNode;
        }
        List<Long> visitedNodes = new ArrayList<>();
        Long crtNode = keyLastNode;
        //start counting from the deepest node up until the first one, using the parents map
        while(crtNode != -1L) {
            visitedNodes.add(crtNode);
            crtNode = Long.valueOf(visited.get(crtNode));
        }


        return visitedNodes;
    }
}
