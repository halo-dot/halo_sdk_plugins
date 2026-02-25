import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

interface Props {
  amount: string;
}

export default function AmountDisplay({ amount }: Props) {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>{amount || '0'}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 2,
    justifyContent: 'center',
    alignItems: 'flex-end',
    paddingHorizontal: 12,
  },
  text: {
    fontSize: 40,
    fontWeight: '300',
    color: '#000',
  },
});
