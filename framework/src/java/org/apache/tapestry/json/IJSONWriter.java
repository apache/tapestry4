// Copyright 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.json;

import java.util.Iterator;

/**
 * JavaScript Object Notation writer interface that defines an object capable of
 * writing JSON style output. 
 * 
 * @see <a href="http://www.json.org/">http://www.json.org/</a>
 * @author JSON.org, jkuhnert
 */
public interface IJSONWriter
{
    /**
     * Accumulate values under a key. It is similar to the put method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there
     * is already a JSONArray, then the new value is appended to it. In
     * contrast, the put method replaces the previous value.
     * 
     * @param key
     *            A key string.
     * @param value
     *            An object to be accumulated under the key.
     * @return this.
     * @throws NullPointerException
     *             if the key is null
     */
    IJSONWriter accumulate(String key, Object value);

    /**
     * Get the value object associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The object associated with the key.
     * @exception NoSuchElementException
     *                if the key is not found.
     */
    Object get(String key);

    /**
     * Get the boolean value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The truth.
     * @exception NoSuchElementException
     *                if the key is not found.
     * @exception ClassCastException
     *                if the value is not a Boolean or the String "true" or
     *                "false".
     */
    boolean getBoolean(String key);

    /**
     * Get the double value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The numeric value.
     * @exception NumberFormatException
     *                if the value cannot be converted to a number.
     * @exception NoSuchElementException
     *                if the key is not found or if the value is a Number
     *                object.
     */
    double getDouble(String key);

    /**
     * Get the int value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return The integer value.
     * @exception NoSuchElementException
     *                if the key is not found
     * @exception NumberFormatException
     *                if the value cannot be converted to a number.
     */
    int getInt(String key);

    /**
     * Get the JSONArray value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return A JSONArray which is the value.
     * @exception NoSuchElementException
     *                if the key is not found or if the value is not a
     *                JSONArray.
     */
    JSONArray getJSONArray(String key);

    /**
     * Get the JSONObject value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return A JSONObject which is the value.
     * @exception NoSuchElementException
     *                if the key is not found or if the value is not a
     *                JSONObject.
     */
    IJSONWriter getJSONObject(String key);

    /**
     * Get the string associated with a key.
     * 
     * @param key
     *            A key string.
     * @return A string which is the value.
     * @exception NoSuchElementException
     *                if the key is not found.
     */
    String getString(String key);

    /**
     * Determine if the JSONObject contains a specific key.
     * 
     * @param key
     *            A key string.
     * @return true if the key exists in the JSONObject.
     */
    boolean has(String key);

    /**
     * Determine if the value associated with the key is null or if there is no
     * value.
     * 
     * @param key
     *            A key string.
     * @return true if there is no value associated with the key or if the value
     *         is the JSONObject.NULL object.
     */
    boolean isNull(String key);

    /**
     * Get an enumeration of the keys of the JSONObject.
     * 
     * @return An iterator of the keys.
     */
    Iterator keys();

    /**
     * Get the number of keys stored in the JSONObject.
     * 
     * @return The number of keys in the JSONObject.
     */
    int length();

    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     * 
     * @return A JSONArray containing the key strings, or null if the JSONObject
     *         is empty.
     */
    JSONArray names();

    /**
     * Get an optional value associated with a key.
     * 
     * @param key
     *            A key string.
     * @return An object which is the value, or null if there is no value.
     * @exception NullPointerException
     *                The key must not be null.
     */
    Object opt(String key);

    /**
     * Get an optional boolean associated with a key. It returns false if there
     * is no such key, or if the value is not Boolean.TRUE or the String "true".
     * 
     * @param key
     *            A key string.
     * @return The truth.
     */
    boolean optBoolean(String key);

    /**
     * Get an optional boolean associated with a key. It returns the
     * defaultValue if there is no such key, or if it is not a Boolean or the
     * String "true" or "false" (case insensitive).
     * 
     * @param key
     *            A key string.
     * @param defaultValue
     *            The default.
     * @return The truth.
     */
    boolean optBoolean(String key, boolean defaultValue);

    /**
     * Get an optional double associated with a key, or NaN if there is no such
     * key or if its value is not a number. If the value is a string, an attempt
     * will be made to evaluate it as a number.
     * 
     * @param key
     *            A string which is the key.
     * @return An object which is the value.
     */
    double optDouble(String key);

    /**
     * Get an optional double associated with a key, or the defaultValue if
     * there is no such key or if its value is not a number. If the value is a
     * string, an attempt will be made to evaluate it as a number.
     * 
     * @param key
     *            A key string.
     * @param defaultValue
     *            The default.
     * @return An object which is the value.
     */
    double optDouble(String key, double defaultValue);

    /**
     * Get an optional int value associated with a key, or zero if there is no
     * such key or if the value is not a number. If the value is a string, an
     * attempt will be made to evaluate it as a number.
     * 
     * @param key
     *            A key string.
     * @return An object which is the value.
     */
    int optInt(String key);

    /**
     * Get an optional int value associated with a key, or the default if there
     * is no such key or if the value is not a number. If the value is a string,
     * an attempt will be made to evaluate it as a number.
     * 
     * @param key
     *            A key string.
     * @param defaultValue
     *            The default.
     * @return An object which is the value.
     */
    int optInt(String key, int defaultValue);

    /**
     * Get an optional JSONArray associated with a key. It returns null if there
     * is no such key, or if its value is not a JSONArray.
     * 
     * @param key
     *            A key string.
     * @return A JSONArray which is the value.
     */
    JSONArray optJSONArray(String key);

    /**
     * Get an optional JSONObject associated with a key. It returns null if
     * there is no such key, or if its value is not a JSONObject.
     * 
     * @param key
     *            A key string.
     * @return A JSONObject which is the value.
     */
    IJSONWriter optJSONObject(String key);

    /**
     * Get an optional string associated with a key. It returns an empty string
     * if there is no such key. If the value is not a string and is not null,
     * then it is coverted to a string.
     * 
     * @param key
     *            A key string.
     * @return A string which is the value.
     */
    String optString(String key);

    /**
     * Get an optional string associated with a key. It returns the defaultValue
     * if there is no such key.
     * 
     * @param key
     *            A key string.
     * @param defaultValue
     *            The default.
     * @return A string which is the value.
     */
    String optString(String key, String defaultValue);

    /**
     * Put a key/boolean pair in the JSONObject.
     * 
     * @param key
     *            A key string.
     * @param value
     *            A boolean which is the value.
     * @return this.
     */
    IJSONWriter put(String key, boolean value);
    
    /**
     * Put a key/double pair in the JSONObject.
     * 
     * @param key
     *            A key string.
     * @param value
     *            A double which is the value.
     * @return this.
     */
    IJSONWriter put(String key, double value);

    /**
     * Put a key/int pair in the JSONObject.
     * 
     * @param key
     *            A key string.
     * @param value
     *            An int which is the value.
     * @return this.
     */
    IJSONWriter put(String key, int value);

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the
     * key will be removed from the JSONObject if it is present.
     * 
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject,
     *            String, or the JSONObject.NULL object.
     * @return this.
     * @exception NullPointerException
     *                The key must be non-null.
     */
    IJSONWriter put(String key, Object value);
    
    /**
     * Put a key/value pair in the JSONObject, but only if the value is
     * non-null.
     * 
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject,
     *            String, or the JSONObject.NULL object.
     * @return this.
     * @exception NullPointerException
     *                The key must be non-null.
     */
    IJSONWriter putOpt(String key, Object value);

    /**
     * Remove a name and its value, if present.
     * 
     * @param key
     *            The name to be removed.
     * @return The value that was associated with the name, or null if there was
     *         no value.
     */
    Object remove(String key);

    /**
     * Produce a JSONArray containing the values of the members of this
     * JSONObject.
     * 
     * @param names
     *            A JSONArray containing a list of key strings. This determines
     *            the sequence of the values in the result.
     * @return A JSONArray of values.
     */
    JSONArray toJSONArray(JSONArray names);

    /**
     * Make an JSON external form string of this JSONObject. For compactness, no
     * unnecessary whitespace is added.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    String toString();

    /**
     * Make a prettyprinted JSON external form string of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     * 
     * @param indentFactor
     *            The number of spaces to add to each level of indentation.
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    String toString(int indentFactor);
    
    /**
     * Causes any un-ended blocks to be closed, as well as 
     * any reasources associated with writer to be flushed/written.
     */
    void close();
    
    /**
     * Allows access to the underlying {@link JSONObject} being used to
     * write content.
     * @return The {@link JSONObject} being delegated to.
     */
    JSONObject getJSONSource();
}
