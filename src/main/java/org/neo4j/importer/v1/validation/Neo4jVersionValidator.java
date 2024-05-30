package org.neo4j.importer.v1.validation;

import org.neo4j.importer.v1.distribution.Neo4jDistribution;
import org.neo4j.importer.v1.targets.NodeTarget;
import org.neo4j.importer.v1.targets.RelationshipTarget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Neo4jVersionValidator implements SpecificationValidator {
    private static final String ERROR_CODE = "VERS-001";

    private final Neo4jDistribution neo4jDistribution;
    private final Map<String, List<String>> unsupportedPaths;

    public Neo4jVersionValidator(Neo4jDistribution neo4jDistribution) {
        this.neo4jDistribution = neo4jDistribution;
        this.unsupportedPaths = new LinkedHashMap<>();
    }

    @Override
    public void visitNodeTarget(int index, NodeTarget nodeTarget) {
        var schema = nodeTarget.getSchema();
        if (schema == null) {
            return;
        }
        var unsupportedFeatures = new ArrayList<String>();
        if (!isEmpty(schema.getTypeConstraints()) && !neo4jDistribution.hasNodeTypeConstraints()) {
            unsupportedFeatures.add("type_constraints");
        }
        if (!isEmpty(schema.getKeyConstraints()) && !neo4jDistribution.hasNodeKeyConstraints()) {
            unsupportedFeatures.add("key_constraints");
        }
        if (!isEmpty(schema.getUniqueConstraints()) && !neo4jDistribution.hasNodeUniqueConstraints()) {
            unsupportedFeatures.add("unique_constraints");
        }
        if (!isEmpty(schema.getExistenceConstraints()) && !neo4jDistribution.hasNodeExistenceConstraints()) {
            unsupportedFeatures.add("existence_constraints");
        }
        if (!isEmpty(schema.getRangeIndexes()) && !neo4jDistribution.hasNodeRangeIndexes()) {
            unsupportedFeatures.add("range_indexes");
        }
        if (!isEmpty(schema.getTextIndexes()) && !neo4jDistribution.hasNodeTextIndexes()) {
            unsupportedFeatures.add("text_indexes");
        }
        if (!isEmpty(schema.getPointIndexes()) && !neo4jDistribution.hasNodePointIndexes()) {
            unsupportedFeatures.add("text_indexes");
        }
        if (!isEmpty(schema.getFullTextIndexes()) && !neo4jDistribution.hasNodeFullTextIndexes()) {
            unsupportedFeatures.add("fulltext_indexes");
        }
        if (!isEmpty(schema.getVectorIndexes()) && !neo4jDistribution.hasNodeVectorIndexes()) {
            unsupportedFeatures.add("vector_indexes");
        }

        if (!unsupportedFeatures.isEmpty()) {
            unsupportedPaths.put(String.format("$.targets.nodes[%d].schema", index), unsupportedFeatures);
        }
    }

    @Override
    public void visitRelationshipTarget(int index, RelationshipTarget relationshipTarget) {
        var schema = relationshipTarget.getSchema();
        if (schema == null) {
            return;
        }

        var unsupportedFeatures = new ArrayList<String>();
        if (!isEmpty(schema.getTypeConstraints()) && !neo4jDistribution.hasRelationshipTypeConstraints()) {
            unsupportedFeatures.add("type_constraints");
        }
        if (!isEmpty(schema.getKeyConstraints()) && !neo4jDistribution.hasRelationshipKeyConstraints()) {
            unsupportedFeatures.add("key_constraints");
        }
        if (!isEmpty(schema.getUniqueConstraints()) && !neo4jDistribution.hasRelationshipUniqueConstraints()) {
            unsupportedFeatures.add("unique_constraints");
        }
        if (!isEmpty(schema.getExistenceConstraints()) && !neo4jDistribution.hasRelationshipExistenceConstraints()) {
            unsupportedFeatures.add("existence_constraints");
        }
        if (!isEmpty(schema.getRangeIndexes()) && !neo4jDistribution.hasRelationshipRangeIndexes()) {
            unsupportedFeatures.add("range_indexes");
        }
        if (!isEmpty(schema.getTextIndexes()) && !neo4jDistribution.hasRelationshipTextIndexes()) {
            unsupportedFeatures.add("text_indexes");
        }
        if (!isEmpty(schema.getPointIndexes()) && !neo4jDistribution.hasRelationshipPointIndexes()) {
            unsupportedFeatures.add("text_indexes");
        }
        if (!isEmpty(schema.getFullTextIndexes()) && !neo4jDistribution.hasRelationshipFullTextIndexes()) {
            unsupportedFeatures.add("fulltext_indexes");
        }
        if (!isEmpty(schema.getVectorIndexes()) && !neo4jDistribution.hasRelationshipVectorIndexes()) {
            unsupportedFeatures.add("vector_indexes");
        }

        if (!unsupportedFeatures.isEmpty()) {
            unsupportedPaths.put(String.format("$.targets.relationships[%d].schema", index), unsupportedFeatures);
        }
    }

    @Override
    public boolean report(SpecificationValidationResult.Builder builder) {
        unsupportedPaths.forEach((path, features) -> builder.addError(
                path,
                ERROR_CODE,
                String.format("%s, features are not supported by %s.", features, neo4jDistribution.toString())));
        return !unsupportedPaths.isEmpty();
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
