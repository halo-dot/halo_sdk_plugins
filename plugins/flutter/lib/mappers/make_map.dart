Map<T1, T2>? makeMap<T1, T2>(Map<dynamic, dynamic>? data) {
  if (data == null) {
    return null;
  }
  return data.map((key, value) => MapEntry(key as T1, value as T2));
}
