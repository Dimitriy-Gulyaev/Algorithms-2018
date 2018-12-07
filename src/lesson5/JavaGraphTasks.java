package lesson5;

import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {

    /**
     * Эйлеров цикл.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     * <p>
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */

    // Трудоёмкость самого алгоритма T = O(V)
    // Трудоёмкость обхода при проверке T = O(V + E)
    // Ресурсоёмкость R = O(V)
    // V - количество вершин в графе, E - количество рёбер в графе
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        Graph.Vertex vertex = (Graph.Vertex) graph.getVertices().toArray()[0];
        if (!checkForEulerPath(graph, vertex)) return new ArrayList<>();
        List<Graph.Edge> edges = new ArrayList<>(graph.getEdges()); //Список всех рёбер для проверки на удалённые
        List<Graph.Edge> result = new ArrayList<>();
        Stack<Graph.Vertex> stack = new Stack<>();
        Graph.Vertex previous = null;

        /* Стандартный алгоритм добавления следующей вершины в стэк и "удаления" ребра между текущей и следующей
          вершиной. Если у вершины больше нет доступных рёбер, ребро между ней и предыдущей добавлется в ответ.
         */
        stack.push(vertex);
        while (!stack.empty()) {
            vertex = stack.peek();
            int degreeOfVertex = 0;
            Map<Graph.Vertex, Graph.Edge> neighbours = graph.getConnections(vertex);
            List<Graph.Edge> neighbourEdges = new ArrayList<>(neighbours.values());
            for (Graph.Edge edge : neighbourEdges)
                if (edges.contains(edge)) degreeOfVertex++;
            if (degreeOfVertex == 0) {
                if (previous == null) previous = stack.pop();
                else {
                    result.add(neighbours.get(previous));
                    previous = vertex;
                    stack.pop();
                }
            } else {
                int i = 0;
                Graph.Edge e = neighbourEdges.get(i);
                while (!edges.contains(e)) {
                    i++;
                    e = neighbourEdges.get(i);
                }
                assert e != null;
                if (vertex == e.getBegin())
                    stack.push(e.getEnd());
                else
                    stack.push(e.getBegin());

                edges.remove(e);
            }
        }
        return result;
    }

    // Проверка графа на эйлеровость (связность графа и четность степени вершин)
    private static boolean checkForEulerPath(Graph graph, Graph.Vertex vertex) {
        List<Graph.Vertex> visited = new ArrayList<>();
        return checkForEulerPathRecursive(vertex, visited, graph);
    }

    private static boolean checkForEulerPathRecursive(Graph.Vertex vertex, List<Graph.Vertex> visited, Graph graph) {
        visited.add(vertex);
        if (graph.getConnections(vertex).size() % 2 != 0) return false;
        for (Graph.Vertex neighbor : graph.getNeighbors(vertex)) {
            if (!visited.contains(neighbor))
                checkForEulerPathRecursive(neighbor, visited, graph);
        }
        return visited.size() == graph.getVertices().size();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ:
     * <p>
     * G    H
     * |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */

    // Трудоёмкость самого алгоритма T = O(V)
    // Трудоёмкость обхода при проверке T = O(V + E)
    // Ресурсоёмкость R = O(V)
    public static Graph minimumSpanningTree(Graph graph) {
        List<Graph.Vertex> vertices = new ArrayList<>(graph.getVertices());
        GraphBuilder builder = new GraphBuilder();
        if (!checkIfConnectedGraph(graph)) return builder.build();
        Graph.Vertex vertex = vertices.get(0);
        List<Graph.Vertex> addedToGraph = new ArrayList<>();
        Stack<Graph.Vertex> stack = new Stack<>();
        stack.push(vertex);
        addedToGraph.add(vertex);
        builder.addVertex(vertex.getName());

        while (!stack.empty()) {
            vertex = stack.peek();
            List<Graph.Edge> neighbourEdges = new ArrayList<>(graph.getConnections(vertex).values());
            Graph.Vertex next = null;
            for (Graph.Edge edge : neighbourEdges) {
                if (vertex == edge.getBegin() && !addedToGraph.contains(edge.getEnd())) {
                    next = edge.getEnd();
                    addedToGraph.add(next);
                    stack.push(next);
                    builder.addVertex(next.getName());
                    builder.addConnection(vertex, next, 1);
                    break;
                }
            }
            if (next == null) stack.pop();
        }

        return builder.build();
    }

    // Проверка графа на связность
    private static boolean checkIfConnectedGraph(Graph graph) {
        List<Graph.Vertex> visited = new ArrayList<>();
        Graph.Vertex vertex = (Graph.Vertex) graph.getVertices().toArray()[0];
        return checkIfConnectedGraphRecursive(vertex, visited, graph);
    }

    private static boolean checkIfConnectedGraphRecursive(Graph.Vertex vertex,
                                                          List<Graph.Vertex> visited, Graph graph) {
        visited.add(vertex);
        for (Graph.Vertex neighbor : graph.getNeighbors(vertex)) {
            if (!visited.contains(neighbor))
                checkIfConnectedGraphRecursive(neighbor, visited, graph);
        }
        return visited.size() == graph.getVertices().size();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     * <p>
     * Дан граф без циклов (получатель), например
     * <p>
     * G -- H -- J
     * |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     * <p>
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     * <p>
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     * <p>
     * В данном случае ответ (A, E, F, D, G, J)
     * <p>
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */

    // Трудоёмкость T = O(V + E)
    // Ресурсоёмкость R = O(V)
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        // Если граф связный, то просто применяем к нему фукнцию
        if (checkIfConnectedGraph(graph)) return
                largestIndependentVertexSetForConnectedComponent(graph);
            // Если в графе есть несколько компонент связности, разбиваем граф по компонентам на отдельные графы
            // (строим их, не изменяя граф-получатель) и применяем функцию к каждому из них, суммируем результат.
        else {
            Set<Graph.Vertex> result = new HashSet<>();
            for (Graph connectedComponent : getConnectedComponentsSet(graph))
                result.addAll(largestIndependentVertexSetForConnectedComponent(connectedComponent));
            return result;
        }
    }

    // Т.к. по условию нам дан граф без циклов, у каждой его компоненты связности будет всего два максимальных
    // независимых множества, поэтому достаточно выбрать любые две соседние вершины и формировать эти множества от них,
    // двигаясь через одну вершину к концу графа.
    private static Set<Graph.Vertex> largestIndependentVertexSetForConnectedComponent(Graph graph) {
        List<Graph.Vertex> vertices = new ArrayList<>(graph.getVertices());
        Set<Graph.Vertex> set1 = new HashSet<>();
        Set<Graph.Vertex> set2 = new HashSet<>();
        Graph.Vertex firstVertex = (Graph.Vertex) graph.getVertices().toArray()[0];
        Graph.Vertex secondVertex = (Graph.Vertex) graph.getConnections(firstVertex).keySet().toArray()[0];
        buildSet(firstVertex, set1, graph, null);
        buildSet(secondVertex, set2, graph, null);
        //noinspection SuspiciousMethodCalls
        if (vertices.indexOf(set1.toArray()[0]) < vertices.indexOf(set2.toArray()[0])) return set1;
        else return set2;
    }

    private static void buildSet(Graph.Vertex vertex, Set<Graph.Vertex> set, Graph graph, Graph.Vertex previous) {
        set.add(vertex);
        List<Graph.Vertex> neighbourVertices = new ArrayList<>(graph.getConnections(vertex).keySet());
        if (previous != null) neighbourVertices.remove(previous);
        for (Graph.Vertex neighbour : neighbourVertices) {
            List<Graph.Vertex> grandNeighbours = new ArrayList<>(graph.getConnections(neighbour).keySet());
            grandNeighbours.remove(vertex);
            for (Graph.Vertex grandNeighbour : grandNeighbours) {
                buildSet(grandNeighbour, set, graph, neighbour);
            }
        }
    }

    // Получаем множество всех компонент связности графа в виде множества графов.
    // Для каждой компоненты строится отдельный граф
    private static Set<Graph> getConnectedComponentsSet(Graph graph) {
        Set<Graph> result = new HashSet<>();
        List<Graph.Vertex> vertices = new ArrayList<>(graph.getVertices());
        while (vertices.size() != 0) {
            GraphBuilder builder = new GraphBuilder();
            if (vertices.size() < 2) {
                builder.addVertex(vertices.get(0).getName());
                result.add(builder.build());
                break;
            }
            Graph.Vertex previous = vertices.get(0);
            builder.addVertex(previous.getName());
            vertices.remove(previous);
            for (Graph.Vertex vertex : graph.getConnections(previous).keySet()) {
                getConnectedComponentsSetRecursive(graph, vertex, previous, builder, vertices);
            }
            result.add(builder.build());
        }
        return result;
    }

    private static void getConnectedComponentsSetRecursive(Graph graph, Graph.Vertex vertex, Graph.Vertex previous,
                                                           GraphBuilder builder, List<Graph.Vertex> vertices) {
        builder.addVertex(vertex.getName());
        builder.addConnection(previous, vertex, 1);
        vertices.remove(vertex);
        Map<Graph.Vertex, Graph.Edge> neighbours = graph.getConnections(vertex);
        for (Graph.Vertex neighbour : neighbours.keySet()) {
            if (vertices.contains(neighbour))
                getConnectedComponentsSetRecursive(graph, neighbour, vertex, builder, vertices);
        }

    }


    /**
     * Наидлиннейший простой путь.
     * Сложная
     * <p>
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */

    // Трудоёмкость T = O(V * (V + E))
    // Ресурсоёмкость R = O(V)
    public static Path longestSimplePath(Graph graph) {
        if (checkIfConnectedGraph(graph)) return longestSimplePathForConnectedComponent(graph);
        else {
            List<Path> paths = new ArrayList<>();
            for (Graph connectedComponent : getConnectedComponentsSet(graph))
                paths.add(longestSimplePathForConnectedComponent(connectedComponent));
            Comparator<Path> c = Comparator.comparing(Path::getLength);
            paths.sort(c);
            return paths.get(paths.size() - 1);
        }
    }

    private static Path longestSimplePathForConnectedComponent(Graph graph) {
        Set<Graph.Vertex> vertices = new HashSet<>(graph.getVertices());
        Path longestPath = new Path(new LinkedList<>(), 0);
        for (Graph.Vertex vertex : vertices) {
            Set<Graph.Vertex> visited = new HashSet<>();
            LinkedList<Graph.Vertex> path = new LinkedList<>();
            List<Path> paths = new ArrayList<>();
            DepthFirstSearch(graph, visited, vertex, path, paths);
            Comparator<Path> c = Comparator.comparing(Path::getLength);
            paths.sort(c);
            Path localLongestPath = paths.get(paths.size() - 1);
            if (localLongestPath.compareTo(longestPath) > 0) longestPath = localLongestPath;
        }
        return longestPath;
    }

    private static void DepthFirstSearch(Graph graph, Set<Graph.Vertex> visited, Graph.Vertex vertex,
                                         LinkedList<Graph.Vertex> path, List<Path> paths) {
        visited.add(vertex);
        path.add(vertex);
        Set<Graph.Vertex> neighbours = new HashSet<>(graph.getConnections(vertex).keySet());
        int visitedNeighbours = 0;
        for (Graph.Vertex neighbour : neighbours) {
            if (visited.contains(neighbour)) {
                visitedNeighbours++;
                continue;
            }
            DepthFirstSearch(graph, visited, neighbour, path, paths);
        }
        if (visitedNeighbours == neighbours.size()) paths.add(new Path(path, path.size() - 1));
    }
}
