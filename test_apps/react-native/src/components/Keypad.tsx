import React from 'react';
import { View, Text, TouchableHighlight, StyleSheet } from 'react-native';

interface Props {
  amount: string;
  setAmount: (value: string) => void;
}

const KEYS = [
  ['1', '2', '3'],
  ['4', '5', '6'],
  ['7', '8', '9'],
  ['.', '0', 'C'],
];

export default function Keypad({ amount, setAmount }: Props) {
  function onPress(key: string) {
    if (key === 'C') {
      setAmount('');
    } else {
      setAmount(amount + key);
    }
  }

  return (
    <View style={styles.container}>
      {KEYS.map((row, rowIndex) => (
        <View key={rowIndex} style={styles.row}>
          {row.map(key => (
            <TouchableHighlight
              key={key}
              style={styles.key}
              underlayColor="#ADD8E6"
              onPress={() => onPress(key)}>
              <Text style={styles.keyText}>{key}</Text>
            </TouchableHighlight>
          ))}
        </View>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 6,
  },
  row: {
    flex: 1,
    flexDirection: 'row',
  },
  key: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  keyText: {
    fontSize: 22,
    color: '#000',
  },
});
