/*
 * Copyright 2015-2018 _floragunn_ GmbH
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 *
 * Modifications Copyright OpenSearch Contributors. See
 * GitHub history for details.
 */

package org.opensearch.security.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapUtils {

    public static void deepTraverseMap(final Map<String, Object> map, final Callback cb) {
        deepTraverseMap(map, cb, null);
    }

    private static void deepTraverseMap(final Map<String, Object> map, final Callback cb, final List<String> stack) {
        final List<String> localStack;
        if(stack == null) {
            localStack = new ArrayList<String>(30);
        } else {
            localStack = stack;
        }
        for(Map.Entry<String, Object> entry: map.entrySet()) {
            if(entry.getValue() != null && entry.getValue() instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> inner = (Map<String, Object>) entry.getValue();
                localStack.add(entry.getKey());
                deepTraverseMap(inner, cb, localStack);
                if(!localStack.isEmpty()) {
                    localStack.remove(localStack.size()-1);
                }
            } else {
                cb.call(entry.getKey(), map, Collections.unmodifiableList(localStack));
            }
        }
    }

    public static interface Callback {
        public void call(String key, Map<String, Object> map, List<String> stack);
    }
}
