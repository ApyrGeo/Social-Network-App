package ubb.scs.map.utils;

import java.util.*;

public class Graph {

    public void DFS(Map<Long, ArrayList<Long>> nodes, Map<Long, Integer> visited, Long currentNode, Integer componentNr) {
        visited.put(currentNode, componentNr);
        nodes.get(currentNode).forEach(n -> {
            if (visited.get(n) == 0) {
                DFS(nodes, visited, n, componentNr);
            }
        });
    }

    public List<Long> BFS(Map<Long, ArrayList<Long>> nodes, Map<Long, Integer> visited, Long startingNode) {
        Queue<Long> queue = new LinkedList<>();
        queue.add(startingNode);

        List<Long> visitedNodes = new ArrayList<>();
        while (!queue.isEmpty()) {

            Long currentNode = queue.poll();
            visitedNodes.add(currentNode);
            visited.put(currentNode, 1);

            nodes.get(currentNode).forEach(n -> {
                if(visited.get(n) == 0)
                    queue.add(n);
            });
        }
        return visitedNodes;
    }
}
